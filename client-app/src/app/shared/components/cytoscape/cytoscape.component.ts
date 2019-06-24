import {Component, OnChanges, ElementRef, Input, Output, EventEmitter, Renderer2} from '@angular/core';


declare var cytoscape: any;


@Component({
  selector: 'app-cytoscape',
  template: '<div id="cy"></div>',
  styles: [`#cy {
    height: 100%;
    width: 100%;
    position: relative;
    left: 0;
    top: 0;
  }`]
})


export class CytoscapeComponent implements OnChanges {

  @Input() public elements: any;
  @Input() public style: any;
  @Input() public layout: any;
  @Input() public zoom: any;
  @Input() public version: number;
  @Output() public specialRoutine: EventEmitter<any> = new EventEmitter<any>();

  @Output() select: EventEmitter<any> = new EventEmitter<any>();

  public constructor(private renderer: Renderer2, private el: ElementRef) {

    this.layout = this.layout || {
      name: 'grid',
      directed: true,
      padding: 0
    };

    this.zoom = this.zoom || {
      min: 0.1,
      max: 1.5
    };

    this.style = this.style || cytoscape.stylesheet()

      .selector('node')
      .css({
        shape: 'data(shapeType)',
        width: 'mapData(weight, 40, 80, 20, 60)',
        content: 'data(name)',
        'text-valign': 'center',
        'text-outline-width': 1,
        'text-outline-color': 'data(colorCode)',
        'background-color': 'data(colorCode)',
        color: '#fff',
        'font-size': 10
      })
      .selector(':selected')
      .css({
        'border-width': 1,
        'border-color': 'black'
      })
      .selector('edge')
      .css({
        'curve-style': 'bezier',
        opacity: 0.666,
        width: 'mapData(strength, 70, 100, 2, 6)',
        'target-arrow-shape': 'triangle',
        'line-color': 'data(colorCode)',
        'source-arrow-color': 'data(colorCode)',
        'target-arrow-color': 'data(colorCode)'
      })
      .selector('edge.questionable')
      .css({
        'line-style': 'dotted',
        'target-arrow-shape': 'diamond'
      })
      .selector('.faded')
      .css({
        opacity: 0.25,
        'text-opacity': 0
      });
  }

  public ngOnChanges(): any {
    this.render();
    console.log(this.el.nativeElement);
  }

  public render() {
    const cyContainer = this.renderer.selectRootElement('#cy');
    const localselect = this.select;
    const localSpecialRoutine = this.specialRoutine;
    const cy = cytoscape({
      container: cyContainer,
      layout: this.layout,
      minZoom: this.zoom.min,
      maxZoom: this.zoom.max,
      style: this.style,
      elements: this.elements
    });


    cy.cxtmenu({
      selector: 'targetNode, edge',
      commands: [
        {
          content: 'Add special routine',
          select(ele) {
            localSpecialRoutine.emit(ele.id());
            console.log(ele.id());
          }
        },
        {
          content: 'View name',
          select(ele) {
            console.log(ele.data('name'));
          }// ,
          // enabled: false IF I WANT TO DISABLE IT
        },
        {
          content: 'Text',
          select(ele) {
            console.log(ele.position());
          }
        },
        {
          content: 'View id',
          select(ele) {
            console.log(ele.id());
          }
        },
      ]
    });
    cy.cxtmenu({
      selector: 'core',
      commands: [
        {
          content: 'Lock system',
          select() {
            console.log('LOCKED');
          }
        },
        {
          content: 'Unlock system',
          select() {
            console.log('UNLOCKED');
          }
        }
      ]
    });

    cy.on('tap', 'node', (e) => {
      const node = e.target;
      const neighborhood = node.neighborhood().add(node);

      cy.elements().addClass('faded');
      neighborhood.removeClass('faded');
      localselect.emit(node.data('name'));
    });

    cy.on('tap', (e) => {
      if (e.target === cy) {
        cy.elements().removeClass('faded');
      }
    });
  }

}
