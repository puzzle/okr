import { Component, Input, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js/auto';
import 'chartjs-adapter-moment';
import {
  KeyResultService,
  Measure,
} from '../../shared/services/key-result.service';
import { Observable } from 'rxjs';
import { Goal } from '../../shared/services/goal.service';

Chart.register(...registerables);

export interface DiagramObject {
  x: string;
  y: number;
}

const plugin = {
  id: 'chart_area_background_color',
  beforeDraw: (
    chart: { width?: any; height?: any; ctx?: any },
    args: any,
    options: { color: any }
  ) => {
    const { ctx } = chart;
    ctx.save();
    ctx.globalCompositeOperation = 'destination-over';
    ctx.fillStyle = options.color;
    ctx.fillRect(27, 10, chart.width - 35, chart.height - 62);
    ctx.restore();
  },
  defaults: {
    color: 'lightgray',
  },
};

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.scss'],
})
export class DiagramComponent implements OnInit {
  @Input() goal!: Goal;
  measures!: Observable<Measure[]>;
  diagram!: Chart;

  constructor(private keyresultService: KeyResultService) {}

  ngOnInit(): void {
    this.diagram = this.generateDiagram(
      this.goal.basicValue,
      this.goal.targetValue
    );

    this.measures = this.keyresultService.getMeasuresOfKeyResult(
      this.goal.keyresult.id
    );

    this.measures.subscribe((measures) => {
      this.reloadDiagram(measures);
    });
  }

  generateDiagrammObjects(measures: Measure[]): any[] {
    measures.sort(
      (measureA, measureB) =>
        new Date(measureA.measureDate).getTime() -
        new Date(measureB.measureDate).getTime()
    );

    return measures.map((item) => {
      return { x: item.measureDate, y: item.value } as DiagramObject;
    });
  }

  generateDiagram(
    min: number,
    max: number,
    values: DiagramObject[] = []
  ): Chart {
    return new Chart('myChart', {
      type: 'scatter',
      data: {
        datasets: [
          {
            backgroundColor: ['black'],
            borderColor: ['black'],
            borderWidth: 2,
            label: 'Messung am',
            data: values,
            pointRadius: 5,
            pointStyle: 'circle',
          },
        ],
      },
      options: {
        showLine: true,
        backgroundColor: '#FE242422',
        animation: false,
        spanGaps: true,
        plugins: {
          legend: { display: false },
          tooltip: { enabled: true, displayColors: false },
          chart_area_background_color: {},
        },

        scales: {
          x: {
            type: 'time',
            ticks: {
              source: 'auto',
            },
            time: {
              unit: 'day', //second
              displayFormats: {
                hour: 'dd/MM/yyyy', //second = 'dd/MM/yyyy HH:mm:ss'
              },
              tooltipFormat: 'DD.MM.yyyy',
            },
          },
          y: {
            suggestedMin: Math.min(max, min),
            suggestedMax: Math.max(max, min),
            ticks: {
              callback: function (value) {
                return Number.isInteger(value) ? value : '';
              },
            },
          },
        },
        responsive: true,
      },
      plugins: [plugin],
    });
  }

  public reloadDiagram(measures: Measure[]) {
    this.diagram.data.datasets[0].data = this.generateDiagrammObjects(measures);
    this.diagram.update();
  }
}
