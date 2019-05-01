import {ChangeDetectorRef, Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
import {MatDialog} from '@angular/material';
import {NodeDetailsComponent} from '../node-details/node-details.component';
import {Pos} from '../../../../shared/models/network/gui/Pos';
import {NetworkDebugService} from '../../../../shared/services/network-debug.service';
import {NeuralNodeGui} from '../../../../shared/models/network/gui/NeuralNodeGui';
import {Status} from '../../../../shared/models/network/Status';
import {LayerGui} from '../../../../shared/models/network/gui/LayerGui';
import {LinkGui} from '../../../../shared/models/network/gui/LinkGui';

@Component({
  selector: 'app-network-debugging',
  templateUrl: './network-debugging.component.html',
  styleUrls: ['./network-debugging.component.scss']
})
export class NetworkDebuggingComponent implements OnInit {

  @ViewChild('canvas') canvasRef: ElementRef;
  @ViewChild('contextmenu') contextMenu: ElementRef;
  private canvas: HTMLCanvasElement;
  private context: CanvasRenderingContext2D;
  private width: number;
  private height: number;
  private offsetX: number;
  private offsetY: number;
  private MARGIN_LEFT = 40;
  private MARGIN_RIGHT = 70; // generally you want to take into account the node radius for this
  private MARGIN_TOP = 40;
  private MARGIN_BOTTOM = 40; // generally you want to take into account the node radius for this
  private NODE_RADIUS = NeuralNodeGui.RADIUS; // 15;
  private DISTANCE_BETWEEN_POINTS_X = 100; // 100;
  private DISTANCE_BETWEEN_POINTS_Y = 50; // 50;
  private NETWORK_OFFSET_TOP_INCREMENT = this.DISTANCE_BETWEEN_POINTS_Y;
  private FILL_SCREEN = false;
  public showMenu = false;
  public selection: NeuralNodeGui;
  public loading: boolean;

  constructor(private networkDebugService: NetworkDebugService,
              private renderer: Renderer2,
              public dialog: MatDialog,
              public cdRef: ChangeDetectorRef
  ) {

  }

  ngOnInit() {
    this.loading = true;
    this.initCanvas();
    this.initPoints();
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
  }


  @HostListener('window:resize', ['$event'])
  onResize(event) {
    if (this.FILL_SCREEN === true) {
      this.context.clearRect(0, 0, this.width, this.height);
      this.width = window.innerWidth;
      this.height = window.innerHeight;
      this.offsetX = this.canvas.offsetLeft;
      this.offsetY = this.canvas.offsetTop;
      this.initPoints();
    }
  }


  getNodeByMousePos(pos: Pos): NeuralNodeGui {
    return this.networkDebugService.findNodeForPos(pos);
  }

  getDistance(pos1: Pos, pos2: Pos): number {
    return Math.sqrt(Math.pow(pos2.x - pos1.x, 2) + Math.pow(pos2.y - pos1.y, 2));
  }


  drawNodes() {
    this.networkDebugService.layers.forEach(layer => {
      layer.nodes.forEach(node => {
        node.draw(this.context);
        node.drawText(this.context);
      });
    });
  }

  drawLines() {
    this.networkDebugService.layers.forEach(layer => {
      layer.nodes.forEach(node => {
        node.outputLinks.forEach(link => link.draw(this.context));
      });
    });
  }

  recomputeDistancesToFill() {
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
  }


  initPoints() {

    this.networkDebugService.getConnections().subscribe(connections => {

      this.networkDebugService.getAll().subscribe(networks => {

        networks.forEach(network => {
          this.networkDebugService.loadNetwork(network);
        });

        connections.forEach(connection => {
          let source: LayerGui = null;
          let destination: LayerGui = null;
          this.networkDebugService.layers.forEach(layer => {
            if (layer.id === connection.sourceLayerId) {
              source = layer;
            }
            if (layer.id === connection.destinationLayerId) {
              destination = layer;
            }
          });
          if (source !== null && destination !== null) {
            source.nodes.forEach(src => {
              destination.nodes.forEach(dest => {
                connection.links.forEach(link => {
                  if(src.id === link.sourceId && dest.id === link.destinationId){
                    const linkGui = new LinkGui(link.id,link.weight,link.status,src,dest);
                    this.networkDebugService.networkConnections.push(linkGui);
                    src.addOutputLink(linkGui);
                    dest.addInputLink(linkGui);
                  }
                });
              });
            });
          }
        });





        // TODO CAND VOI ADAUGA MAI MULTE RETELE, NU MA ATING DE LAYERE, CI LE PASTREZ CUM SUMT (I.E. NU MA ATING DE NEXT, PREVIOUS ETC.),
        // TODO IN SCHIMB FAC LA DRAW MODIFICARI CA LAYERELE A,B DE CARE DEPINDE C SA FIE DESENATE IN STANGA (SI DIN MOM. CE NU M-AM ATINS
        // TODO DE NEXT SI PREV ATUNCI NU MAI AM TREABA CU LOGICA AIA (DOAR LA STERS SA NU STERG DE SUS PANA JOS TOT)
        if (this.FILL_SCREEN === true) {
          this.recomputeDistancesToFill();
        }
        let x = this.MARGIN_LEFT;
        let y;
        let yMax = 0;

        this.networkDebugService.layers.forEach(layer => {
          y = this.MARGIN_TOP;
          layer.xPos = x;
          layer.nodes.forEach(node => {
            node.pos = new Pos(x, y);
            y += this.DISTANCE_BETWEEN_POINTS_Y;
          });
          x += this.DISTANCE_BETWEEN_POINTS_X;
          if (y > yMax) {
            yMax = y;
          }
          if(layer.next === null) {
            this.MARGIN_TOP += this.NETWORK_OFFSET_TOP_INCREMENT;
          }
        });

        if (this.FILL_SCREEN === true) {
          this.canvas.width = this.width;
          this.canvas.height = this.height;
        } else {
          this.canvas.width = x + this.MARGIN_LEFT + this.MARGIN_RIGHT;
          this.canvas.height = yMax + this.MARGIN_TOP + this.MARGIN_BOTTOM;
        }

        this.drawLines();
        this.drawNodes();
        this.loading = false;
      });

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

    const node: NeuralNodeGui = this.getNodeByMousePos(pos);
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

  watchNode(node: NeuralNodeGui) {
    this.hideContextMenu(null);
    node.status = Status.UNDER_WATCH;
    const start = node.layer.previous !== null ? node.layer.previous.xPos : node.layer.xPos;
    const end = node.layer.next !== null ? node.layer.next.xPos : node.layer.xPos;
    this.context.clearRect(start, 0, end - start, this.height);

    this.networkDebugService.networkConnections.forEach(c => {
      c.draw(this.context);
      c.source.draw(this.context);
      c.source.drawText(this.context);
      c.destination.draw(this.context);
      c.destination.drawText(this.context);
    });

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

  addBreakpoint(node: NeuralNodeGui) {
    this.hideContextMenu(null);
    node.status = Status.BREAKPOINT;
    const start = node.layer.previous !== null ? node.layer.previous.xPos : node.layer.xPos;
    const end = node.layer.next !== null ? node.layer.next.xPos : node.layer.xPos;
    this.context.clearRect(start, 0, end - start, this.height);

    if (node.layer.next !== null) {
      node.layer.next.nodes.forEach(node => {
        node.status = node.status !== Status.BREAKPOINT ? Status.WAIT : Status.BREAKPOINT;
      });
    }

    this.networkDebugService.networkConnections.forEach(c => {
      c.draw(this.context);
      c.source.draw(this.context);
      c.source.drawText(this.context);
      c.destination.draw(this.context);
      c.destination.drawText(this.context);
    });

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

  removeBreakpoint(node: NeuralNodeGui) {
    this.hideContextMenu(null);
    node.status = Status.IGNORED;
    if (node.layer.previous !== null) {
      node.layer.previous.nodes.forEach(n => {
        if (n.status === Status.BREAKPOINT) {
          node.status = Status.WAIT; //todo make more efficient, i.e. return when found
        }
      });
    }
    let hasAnotherBreakpoint: boolean = false;
    node.layer.nodes.forEach(n => {
      if (n.status === Status.BREAKPOINT) {
        hasAnotherBreakpoint = true; //todo make more efficient, i.e. return when found
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

    this.networkDebugService.networkConnections.forEach(c => {
      c.draw(this.context);
      c.source.draw(this.context);
      c.source.drawText(this.context);
      c.destination.draw(this.context);
      c.destination.drawText(this.context);
    });

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

  ignoreNode(node: NeuralNodeGui) {
    this.hideContextMenu(null);
    node.status = Status.IGNORED;
    const start = node.layer.previous !== null ? node.layer.previous.xPos : node.layer.xPos;
    const end = node.layer.next !== null ? node.layer.next.xPos : node.layer.xPos;
    this.context.clearRect(start, 0, end - start, this.height);

    this.networkDebugService.networkConnections.forEach(c => {
      c.draw(this.context);
      c.source.draw(this.context);
      c.source.drawText(this.context);
      c.destination.draw(this.context);
      c.destination.drawText(this.context);
    });

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
      const node: NeuralNodeGui = this.getNodeByMousePos(pos);
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
