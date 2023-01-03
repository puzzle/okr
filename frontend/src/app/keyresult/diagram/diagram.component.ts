import { Component, Input, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js/auto';
import 'chartjs-adapter-moment';
import {
  KeyResultService,
  Measure,
} from '../../shared/services/key-result.service';
import { Observable } from 'rxjs';

Chart.register(...registerables);

export interface DiagramObject {
  x: string;
  y: number;
}

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.scss'],
})
export class DiagramComponent implements OnInit {
  @Input() keyResultId!: number;
  measures!: Observable<Measure[]>;

  constructor(private keyresultService: KeyResultService) {}

  ngOnInit(): void {
    this.measures = this.keyresultService.getMeasuresOfKeyResult(
      this.keyResultId
    );
    this.measures.subscribe((measures) => {
      this.generateDiagram(measures);
    });
  }

  generateDiagram(measures: Measure[]) {
    let arrayWithPoints: any[] = [];

    measures.forEach(function (item) {
      let point: DiagramObject = { x: item.measureDate, y: item.value };
      arrayWithPoints.push(point);
    });

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
        ctx.fillRect(27, 10, chart.width - 33, chart.height - 39);
        ctx.restore();
      },
      defaults: {
        color: 'lightgray',
      },
    };

    new Chart('myChart', {
      type: 'scatter',
      data: {
        datasets: [
          {
            backgroundColor: ['black'],
            borderColor: ['black'],
            borderWidth: 2,
            label: 'Messung am',
            data: arrayWithPoints,
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
              tooltipFormat: 'DD/MM/yyyy',
            },
          },
          y: {
            min: 0,
          },
        },
        responsive: true,
      },
      plugins: [plugin],
    });
  }
}
