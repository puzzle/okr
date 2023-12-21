import * as go from 'gojs';
import { Router } from "@angular/router";

export class ContinuousForceDirectedLayout extends go.ForceDirectedLayout  {
  private _isObserving: boolean = false;
  private myDiagram: go.Diagram = new go.Diagram();

  constructor(myDiagram: go.Diagram, private router: Router,) {
    super();
    this.myDiagram = myDiagram;
  }

  eventListener() {
    window.addEventListener('DOMContentLoaded',this.init);
  }

  override isFixed(v: go.LayoutVertex) {
    return v.node!.isSelected;
  }

  override doLayout(coll :any) {
    if (!this._isObserving) {
      this._isObserving = true;
      // cacheing the network means we need to recreate it if nodes or links have been added or removed or relinked,
      // so we need to track structural model changes to discard the saved network.
      this.diagram!.addModelChangedListener(e => {
        // modelChanges include a few cases that we don't actually care about, such as
        // "nodeCategory" or "linkToPortId", but we'll go ahead and recreate the network anyway.
        // Also clear the network when replacing the model.
        if (e.modelChange !== "" ||
          (e.change === go.ChangedEvent.Transaction && e.propertyName === "StartingFirstTransaction")) {
          this.network = null;
        }
      });
    }
    var net = this.network;
    if (net === null) {  // the first time, just create the network as normal
      this.network = net = this.makeNetwork(coll);
    } else {  // but on reuse we need to update the LayoutVertex.bounds for selected nodes
      this.diagram!.nodes.each(n => {
        var v = net!.findVertex(n);
        if (v !== null) v.bounds = n.actualBounds;
      });
    }
    // now perform the normal layout
    super.doLayout(coll);
    // doLayout normally discards the LayoutNetwork by setting Layout.network to null;
    // here we remember it for next time
    this.network = net;
  }

  init() {
    const $ = go.GraphObject.make;

    this.myDiagram =
      new go.Diagram("myDiagramDiv",  // must name or refer to the DIV HTML element
        {
          initialAutoScale: go.Diagram.Uniform,  // an initial automatic zoom-to-fit
          contentAlignment: go.Spot.Center,  // align document to the center of the viewport
          layout:
            $(ContinuousForceDirectedLayout,  // automatically spread nodes apart while dragging
              {
                defaultSpringLength: 60,
                defaultElectricalCharge: 150
              }),
          // do an extra layout at the end of a move
          "SelectionMoved": e => e.diagram.layout.invalidateLayout(),
          "InitialLayoutCompleted": e => {
            // if not all Nodes have real locations, force a layout to happen
            if (!e.diagram.nodes.all(n => n.location.isReal())) {
              e.diagram.layoutDiagram(true);
            }
          }
        });

    // dragging a node invalidates the Diagram.layout, causing a layout during the drag
    this.myDiagram.toolManager.draggingTool.doMouseMove = function () {  // method override must be function, not =>
      go.DraggingTool.prototype.doMouseMove.call(this);
      if (this.isActive) {
        this.diagram!.layout.doLayout(this.diagram!.nodes);
      }
    }

    // define each Node's appearance
    this.myDiagram.nodeTemplate =
      $(go.Node, "Auto",  // the whole node panel
        // define the node's outer shape, which will surround the TextBlock
        $(go.Shape, "Circle",
          {
            name: 'BUBBLE',
            fill: "white",
            stroke: null,
            desiredSize: new go.Size(100, 100)
          },
          new go.Binding("fill", "color"),
          new go.Binding("name", "name"),
          new go.Binding("desiredSize", "size")),
        {
          shadowColor: "gray",
          mouseEnter: function (e, node) {
            if (!node.part!.findObject('PUZZLE') && !node.part!.findObject('TEAM')) {
              const goNode = node as go.Node;
              if (goNode) {
                if (!goNode.isSelected) {
                  goNode.isShadowed = true;
                  if (node.part!.findObject('KR')) {
                    const krObject1 = node.part!.findObject("KR") as go.Shape;
                    if (krObject1) {
                      krObject1.fill = "#F7F8F9";
                    }

                    const krObject2 = node.part!.findObject("KR") as go.Shape;
                    if (krObject2) {
                      krObject2.stroke = "#5D6974";
                    }
                  }
                }
              }
            }
          },
          mouseLeave: function (e, node) {
            if (!node.part!.findObject('PUZZLE') && !node.part!.findObject('TEAM')) {
              const goNode = node as go.Node;
              if (goNode) {
                goNode.isShadowed = false;
              }
              if (node.part!.findObject('KR')) {
                const krObject1 = node.part!.findObject("KR") as go.Shape;
                if (krObject1) {
                  krObject1.fill = "#E5E8EB";
                }

                const krObject2 = node.part!.findObject("KR") as go.Shape;
                if (krObject2) {
                  krObject2.stroke = null; // Access stroke property safely
                }
              }
            }
          },
          click: function (e, node) {
            if (!node.part!.findObject('PUZZLE') && !node.part!.findObject('TEAM')) {
              const goNode = node as go.Node;
              if (goNode) {
                goNode.isShadowed = false;
              }
              if (node.part!.findObject('KR')) {
                const krObject1 = node.part!.findObject("KR") as go.Shape;
                if (krObject1) {
                  krObject1.fill = "#E5E8EB";
                }

                const krObject2 = node.part!.findObject("KR") as go.Shape;
                if (krObject2) {
                  krObject2.stroke = null; // Access stroke property safely
                }
              }
            }
          }
        },
        $(go.TextBlock,
          {
            font: "normal 18px Roboto",
            textAlign: "center",
            stroke: 'black',
            maxSize: new go.Size(90, 90),
            wrap: go.TextBlock.WrapDesiredSize
          },
          new go.Binding("text", "text"),
          new go.Binding("font", "font"),
          new go.Binding("margin", "margin"),
          new go.Binding("stroke", "textColor"))
      );
    // the rest of this app is the same as samples/conceptMap.html

    // replace the default Link template in the linkTemplateMap
    this.myDiagram.linkTemplate =
      $(go.Link,  // the whole link panel
        // {
        //   routing: go.Link.AvoidsNodes
        // },
        $(go.Shape,  // the link shape
          {stroke: "#1C355E"}),
        $(go.Shape,  // the arrowhead
          {toArrow: "standard", stroke: null})
      );

    this.myDiagram.addDiagramListener("ObjectSingleClicked",
      (e:any) => {
        var part = e.subject.part;
        if (part.ub.text == 'puzzle') return;
        if (part.ub.key.charAt(0) == 1) return;
        if (!(part instanceof go.Link)) {
          let type = part.ub.key.charAt(0) == 2 ? 'Objective' : 'KeyResult';
          document.getElementById("clickedElement")!.innerHTML = 'It is a : ' + type + ' with text: ' + part.ub.text;
          // this.router.navigate(['keyresult', part.ub.key.substring(1)]);
          this.router.navigate(['keyresult', 21]);
        }
      });

    let diagramData = createBubbles();

    this.myDiagram.model = new go.GraphLinksModel(diagramData[0], diagramData[1]);
  }
}

function createBubbles() {
  var teams = ["BBT", "dev/tre", "mobility"];
  var objectives1 = ["Wir wollen mehr Umsatz", "Die Verrechenbarkeit liegt bei 95%", "Die Energie kommt von erneuerbaren Quellen"];
  var objectives2 = ["Wir wollen mehr Umsatz", "Die Verrechenbarkeit liegt bei 95%", "Die Energie kommt von erneuerbaren Quellen"];
  var keyresults1 = ["Mehr Kaffees pro Tag", "Die Lehrlinge sollen leiser werden", "Eine Katze soll den Eingang bewachen"];
  var keyresults2 = ["Mehr kuchen", "Die Lehrlinge sollen leiser werden", "Eine Katze soll den Eingang bewachen denn wir wollen keine ungewollten Gäste"];
  var keyresults3 = ["Mehr kuchen", "Die Lehrlinge sollen leiser werden", "Eine Katze soll den Eingang bewachen"];
  var keyresults4 = ["Mehr kuchen", "Die Lehrlinge sollen leiser werden", "Eine Katze soll den Eingang bewachen"];
  var keyresults5 = ["Mehr kuchen", "Die Lehrlinge sollen leiser werden", "Eine Katze soll den Eingang bewachen"];
  var keyresults6 = ["Mehr kuchen", "Die Lehrlinge sollen leiser werden", "Eine Katze soll den Eingang bewachen"];

  let counter = 1;
  var nodeDataArray = [];
  var linkDataArray = [];

  // Puzzle bubbles
  let puzzle = {
    key: counter,
    text: 'puzzle',
    font: 'bold 70px OKRFont, sans-serif',
    margin: new go.Margin(20, 0, 0, 0),
    name: 'PUZZLE',
    textColor: 'white',
    color: "#1E5A96",
    size: new go.Size(200, 200)
  };
  nodeDataArray.push(puzzle);
  counter++;

  // Team bubbles
  for (let i = 0; i < teams.length; i++) {
    let team = {
      key: '1' + counter,
      text: teams[i],
      font: 'normal 32px Roboto',
      name: 'TEAM',
      color: "#238BCA",
      size: new go.Size(160, 160)
    };
    nodeDataArray.push(team);
    counter++;
  }

  // Objective bubbles
  for (let i = 0; i < objectives1.length; i++) {
    let objective = {
      key: '2' + counter,
      text: objectives1[i].length > 25 ? split_at_index(objectives1[i], 25) : objectives1[i],
      color: "#2C97A6",
      size: new go.Size(160, 160)};
    nodeDataArray.push(objective);
    counter++;
  }
  for (let i = 0; i < objectives2.length; i++) {
    let objective = {
      key: '2' + counter,
      text: objectives2[i].length > 25 ? split_at_index(objectives2[i], 25) : objectives2[i],
      color: "#2C97A6",
      size: new go.Size(160, 160)};
    nodeDataArray.push(objective);
    counter++;
  }

  // Keyresult bubbles
  for (let i = 0; i < keyresults1.length; i++) {
    let el = {
      key: '3' + counter,
      text: keyresults1[i].length > 20 ? split_at_index(keyresults1[i], 20) : keyresults1[i],
      font: 'normal 14px Roboto',
      name: 'KR',
      color: "#E5E8EB",
      size: new go.Size(120, 120)
    };
    counter++;
    nodeDataArray.push(el);
  }
  for (let i = 0; i < keyresults2.length; i++) {
    let el = {
      key: '3' + counter,
      text: keyresults2[i].length > 20 ? split_at_index(keyresults2[i], 20) : keyresults2[i],
      font: 'normal 14px Roboto',
      name: 'KR',
      color: "#E5E8EB",
      size: new go.Size(120, 120)
    };
    counter++;
    nodeDataArray.push(el);
  }
  for (let i = 0; i < keyresults3.length; i++) {
    let el = {
      key: '3' + counter,
      text: keyresults3[i],
      font: 'normal 14px Roboto',
      name: 'KR',
      color: "#E5E8EB",
      size: new go.Size(120, 120)
    };
    counter++;
    nodeDataArray.push(el);
  }
  for (let i = 0; i < keyresults4.length; i++) {
    let el = {
      key: '3' + counter,
      text: keyresults4[i],
      font: 'normal 14px Roboto',
      name: 'KR',
      color: "#E5E8EB",
      size: new go.Size(120, 120)
    };
    counter++;
    nodeDataArray.push(el);
  }
  for (let i = 0; i < keyresults5.length; i++) {
    let el = {
      key: '3' + counter,
      text: keyresults5[i],
      font: 'normal 14px Roboto',
      name: 'KR',
      color: "#E5E8EB",
      size: new go.Size(120, 120)
    };
    counter++;
    nodeDataArray.push(el);
  }
  for (let i = 0; i < keyresults6.length; i++) {
    let el = {
      key: '3' + counter,
      text: keyresults6[i],
      font: 'normal 14px Roboto',
      name: 'KR',
      color: "#E5E8EB",
      size: new go.Size(120, 120)
    };
    counter++;
    nodeDataArray.push(el);
  }

  // Keyresult to objective
  let zahl = 11;
  for (let i = 0; i < keyresults1.length; i++) {
    let el = {from: '3' + zahl, to: 25};
    zahl++;
    linkDataArray.push(el);
  }
  zahl = 14;
  for (let i = 0; i < keyresults2.length; i++) {
    let el = {from: '3' + zahl, to: 26};
    zahl++;
    linkDataArray.push(el);
  }
  zahl = 17;
  for (let i = 0; i < keyresults3.length; i++) {
    let el = {from: '3' + zahl, to: 27};
    zahl++;
    linkDataArray.push(el);
  }
  zahl = 20;
  for (let i = 0; i < keyresults4.length; i++) {
    let el = {from: '3' + zahl, to: 28};
    zahl++;
    linkDataArray.push(el);
  }
  zahl = 23;
  for (let i = 0; i < keyresults5.length; i++) {
    let el = {from: '3' + zahl, to: 29};
    zahl++;
    linkDataArray.push(el);
  }
  zahl = 26;
  for (let i = 0; i < keyresults6.length; i++) {
    let el = {from: '3' + zahl, to: 210};
    zahl++;
    linkDataArray.push(el);
  }

  // Objective to team
  zahl = 5;
  for (let i = 0; i < objectives1.length; i++) {
    let el = {from: '2' + zahl, to: '1' + 2};
    zahl++;
    linkDataArray.push(el);
  }
  zahl = 8;
  for (let i = 0; i < objectives2.length; i++) {
    let el = {from: '2' + zahl, to: '1' + 3};
    zahl++;
    linkDataArray.push(el);
  }

  // Team to puzzle
  zahl = 2;
  for (let i = 0; i < teams.length; i++) {
    let el = {from: '1' + zahl, to: 1};
    zahl++;
    linkDataArray.push(el);
  }

  return [nodeDataArray, linkDataArray];
}


function split_at_index(value:any, index:any) {
  return value.substring(0, index) + "...";
}
