import {ChangeDetectorRef, Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
import {MatDialog} from '@angular/material';
import {NodeDetailsComponent} from '../node-details/node-details.component';
import {Pos} from '../../../../shared/models/network/gui/Pos';
import {NodeGui} from '../../../../shared/models/network/gui/NodeGui';
import {Status} from '../../../../shared/models/network/gui/Status';
import {NetworkVisualDataService} from '../../../../shared/services/network-visual-data.service';
import {VirtualNetworkDto} from '../../../../shared/models/network/virtual/VirtualNetworkDto';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NetworkConfigureService} from "../../../../shared/services/network-configure.service";

@Component({
  selector: 'app-network-debugging',
  templateUrl: './network-debugging.component.html',
  styleUrls: ['./network-debugging.component.scss']
})
export class NetworkDebuggingComponent implements OnInit {

  @ViewChild('canvas') canvasRef: ElementRef;
  @ViewChild('contextmenu') contextMenu: ElementRef;
  public showMenu = false;
  public selection: NodeGui;
  public loading: boolean;
  // TODO MOVE THOSE
  public virtualNetworkDto: VirtualNetworkDto;
  x: number;
  networkNames: string[] = [];
  virtualNetworkFormGroup: FormGroup;
  private canvas: HTMLCanvasElement;
  private context: CanvasRenderingContext2D;
  private width: number;
  private height: number;
  private offsetX: number;
  private offsetY: number;
  private MARGIN_LEFT = 40;
  private MARGIN_RIGHT = 70; // generally you want to take into account the targetNode radius for this
  private MARGIN_TOP = 40;
  private MARGIN_BOTTOM = 40; // generally you want to take into account the targetNode radius for this
  private NODE_RADIUS = NodeGui.RADIUS; // 15;
  private DISTANCE_BETWEEN_POINTS_X = 100; // 100;
  private DISTANCE_BETWEEN_POINTS_Y = 50; // 50;
  private NETWORK_OFFSET_TOP_INCREMENT = this.DISTANCE_BETWEEN_POINTS_Y;
  private FILL_SCREEN = false;

  constructor(private networkVisualService: NetworkVisualDataService,
              private networkConfigService: NetworkConfigureService,
              private renderer: Renderer2,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              public cdRef: ChangeDetectorRef
  ) {

  }

  ngOnInit() {
    this.loading = true;
    this.initCanvas();
    this.watchLayerToDraw();
    this.virtualNetworkDto = {
      id: null,
      name: null,
      networkName: null,
      networkId: null,
      nLayers: null
    };
    this.networkConfigService.getNetworkNamesForUser(localStorage.getItem('username'))
      .subscribe(names => {
        console.log('got');
        this.networkNames = names;
      }, err => {
        console.log(err);
      }, () => {
        console.log('done');
      });
    this.virtualNetworkFormGroup = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(4)]],
      networkName: ['', [Validators.required, Validators.minLength(4)]]
    });
  }


  initCanvas() {
    this.width = window.innerWidth;
    this.height = window.innerHeight;
    this.canvas = (this.canvasRef.nativeElement as HTMLCanvasElement);
    this.offsetX = this.canvas.offsetLeft;
    this.offsetY = this.canvas.offsetTop;
    this.canvas.addEventListener('click', this.hideContextMenu.bind(this), false);
    this.canvas.addEventListener('contextmenu', this.showContextMenu.bind(this), false);
    this.context = this.canvas.getContext('2d');
    this.context.clearRect(0, 0, this.width, this.height);
    this.canvas.width = this.width;
    this.canvas.height = 3000;
  }


  @HostListener('window:resize', ['$event'])
  onResize(event) {
    if (this.FILL_SCREEN === true) {
      this.context.clearRect(0, 0, this.width, this.height);
      this.width = window.innerWidth;
      this.height = window.innerHeight;
      this.offsetX = this.canvas.offsetLeft;
      this.offsetY = this.canvas.offsetTop;
      // TODO this.initPoints();
    }
  }


  getNodeByMousePos(pos: Pos): NodeGui {
    return this.networkVisualService.findNodeForPos(pos);
  }

  getDistance(pos1: Pos, pos2: Pos): number {
    return Math.sqrt(Math.pow(pos2.x - pos1.x, 2) + Math.pow(pos2.y - pos1.y, 2));
  }


  recomputeDistancesToFill() {
    /*
    let maxNodesPerLayer = 0;

    this.networkDebugService.layers.forEach(layer => {
      const nodesPerLayer = layer.nodes.size;
      if (nodesPerLayer > maxNodesPerLayer) {
        maxNodesPerLayer = nodesPerLayer;
      }
    });
    this.DISTANCE_BETWEEN_POINTS_Y = (this.height - this.MARGIN_TOP - this.MARGIN_BOTTOM - this.NODE_RADIUS) / (maxNodesPerLayer - 1);
    this.DISTANCE_BETWEEN_POINTS_X = (this.width - this.MARGIN_LEFT - this.MARGIN_RIGHT - this.NODE_RADIUS) /
      (this.networkDebugService.layers.size - 1);
      */
  }

  saveAndLoad() {
    const dto: VirtualNetworkDto = {
      id: null,
      name: this.virtualNetworkFormGroup.value.name,
      networkName: this.virtualNetworkFormGroup.value.networkName,
      networkId: null,
      nLayers: null
    };
    this.networkVisualService.create(dto).subscribe(virtualNetworkDto => {
      this.networkVisualService.virtualNetwork = virtualNetworkDto;
      this.networkVisualService.getNextLayer();
    });
  }

  watchLayerToDraw() {
    if (this.FILL_SCREEN === true) {
      this.recomputeDistancesToFill();
    }
    this.x = this.MARGIN_LEFT;

    this.networkVisualService.layerAdded.subscribe(layerGui => {
      let y = this.MARGIN_TOP;
      layerGui.xPos = this.x;
      layerGui.nodes.forEach(node => {
        node.pos = new Pos(this.x, y);
        y += this.DISTANCE_BETWEEN_POINTS_Y;
      });
      this.x += this.DISTANCE_BETWEEN_POINTS_X;
      if (layerGui.previous != null) {
        layerGui.previous.drawLines(this.context);
      }
      layerGui.drawNodes(this.context);
      layerGui.drawText(this.context);
    });
  }

  hideContextMenu(event) {

    if (this.showMenu === true) {
      this.showMenu = false;
      this.renderer.removeClass(this.contextMenu.nativeElement, 'menu-show');
    }
    if (event === null) {
      return;
    }
    const x = event.layerX - this.offsetX;
    const y = event.layerY - this.offsetY;
    const pos: Pos = new Pos(x, y);

    const node: NodeGui = this.getNodeByMousePos(pos);
    if (node != null) {
      this.selection = node;
      this.openDetailsDialog();
    }
  }


  openDetailsDialog(): void {
    const dialogRef = this.dialog.open(NodeDetailsComponent, {
      width: '1900px',
      data: this.selection
    });
  }

  watchNode(node: NodeGui) {
    this.hideContextMenu(null);
    node.status = Status.UNDER_WATCH;
    const start = node.layer.previous !== null ? node.layer.previous.xPos : node.layer.xPos;
    const end = node.layer.next !== null ? node.layer.next.xPos : node.layer.xPos;
    this.context.clearRect(start, 0, end - start, this.height);

    /* TODO
    this.networkDebugService.networkConnections.forEach(c => {
      c.draw(this.context);
      c.source.draw(this.context);
      c.source.drawText(this.context);
      c.destination.draw(this.context);
      c.destination.drawText(this.context);
    });
    */

    node.layer.drawLayer(this.context);
    if (node.layer.previous !== null) {
      node.layer.previous.drawNodes(this.context);
      node.layer.previous.drawText(this.context);
    }
    if (node.layer.next !== null) {
      node.layer.next.drawNodes(this.context);
      node.layer.next.drawNodes(this.context);
    }
  }

  addBreakpoint(node: NodeGui) {
    this.hideContextMenu(null);
    node.status = Status.BREAKPOINT;
    const start = node.layer.previous !== null ? node.layer.previous.xPos : node.layer.xPos;
    const end = node.layer.next !== null ? node.layer.next.xPos : node.layer.xPos;
    this.context.clearRect(start, 0, end - start, this.height);

    if (node.layer.next !== null) {
      node.layer.next.nodes.forEach(n => {
        n.status = n.status !== Status.BREAKPOINT ? Status.WAIT : Status.BREAKPOINT;
      });
    }

    /* TODO
    this.networkDebugService.networkConnections.forEach(c => {
      c.draw(this.context);
      c.source.draw(this.context);
      c.source.drawText(this.context);
      c.destination.draw(this.context);
      c.destination.drawText(this.context);
    });
    */

    node.layer.drawLayer(this.context);
    if (node.layer.previous !== null) {
      node.layer.previous.drawNodes(this.context);
      node.layer.previous.drawText(this.context);
    }
    if (node.layer.next !== null) {
      node.layer.next.drawNodes(this.context);
      node.layer.next.drawNodes(this.context);
    }
  }

  removeBreakpoint(node: NodeGui) {
    this.hideContextMenu(null);
    node.status = Status.IGNORED;
    if (node.layer.previous !== null) {
      node.layer.previous.nodes.forEach(n => {
        if (n.status === Status.BREAKPOINT) {
          node.status = Status.WAIT; // todo make more efficient, i.e. return when found
        }
      });
    }
    let hasAnotherBreakpoint = false;
    node.layer.nodes.forEach(n => {
      if (n.status === Status.BREAKPOINT) {
        hasAnotherBreakpoint = true; // todo make more efficient, i.e. return when found
      }
    });
    if (hasAnotherBreakpoint === false && node.layer.next !== null) {
      node.layer.next.nodes.forEach(n => {
        if (n.status === Status.WAIT) {
          n.status = Status.IGNORED;
        }
      });
    }

    const start = node.layer.previous !== null ? node.layer.previous.xPos : node.layer.xPos;
    const end = node.layer.next !== null ? node.layer.next.xPos : node.layer.xPos;
    this.context.clearRect(start, 0, end - start, this.height);

    /* TODO
    this.networkDebugService.networkConnections.forEach(c => {
      c.draw(this.context);
      c.source.draw(this.context);
      c.source.drawText(this.context);
      c.destination.draw(this.context);
      c.destination.drawText(this.context);
    });
    */

    node.layer.drawLayer(this.context);
    if (node.layer.previous !== null) {
      node.layer.previous.drawNodes(this.context);
      node.layer.previous.drawText(this.context);
    }
    if (node.layer.next !== null) {
      node.layer.next.drawNodes(this.context);
      node.layer.next.drawNodes(this.context);
    }

  }

  ignoreNode(node: NodeGui) {
    this.hideContextMenu(null);
    node.status = Status.IGNORED;
    const start = node.layer.previous !== null ? node.layer.previous.xPos : node.layer.xPos;
    const end = node.layer.next !== null ? node.layer.next.xPos : node.layer.xPos;
    this.context.clearRect(start, 0, end - start, this.height);

    /* TODO
    this.networkDebugService.networkConnections.forEach(c => {
      c.draw(this.context);
      c.source.draw(this.context);
      c.source.drawText(this.context);
      c.destination.draw(this.context);
      c.destination.drawText(this.context);
    });
    */

    node.layer.drawLayer(this.context);
    if (node.layer.previous !== null) {
      node.layer.previous.drawNodes(this.context);
      node.layer.previous.drawText(this.context);
    }
    if (node.layer.next !== null) {
      node.layer.next.drawNodes(this.context);
      node.layer.next.drawNodes(this.context);
    }
  }

  showContextMenu(event) {
    if (this.showMenu === false) {
      event.preventDefault();
      this.showMenu = true;

      const x = event.layerX - this.offsetX;
      const y = event.layerY - this.offsetY;

      let displayAtX = event.layerX;
      let displayAtY = event.layerY;


      if (this.FILL_SCREEN === true) {
        if (displayAtY + 300 > this.height) {
          displayAtY = this.height - 300;
        }
        if (displayAtX + 400 > this.width) {
          displayAtX = this.width - 400;
        }

      }
      const pos: Pos = new Pos(x, y);
      const node: NodeGui = this.getNodeByMousePos(pos);
      if (node != null) {
        this.selection = node;
        this.renderer.addClass(this.contextMenu.nativeElement, 'menu-show');
        this.renderer.setStyle(this.contextMenu.nativeElement, 'top', displayAtY + 'px');
        this.renderer.setStyle(this.contextMenu.nativeElement, 'left', displayAtX + 'px');
      }
    } else {
      event.preventDefault();
      this.showMenu = false;
      this.renderer.removeClass(this.contextMenu.nativeElement, 'menu-show');
    }
  }

}
