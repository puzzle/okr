import { Component, Input, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js/auto';
import 'chartjs-adapter-moment';
import {
  KeyResultService,
  Measure,
} from '../../shared/services/key-result.service';
import { Observable } from 'rxjs';

Chart.register(...registerables);

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
      console.log(measures);

      this.generateDiagram(measures);
    });
  }

  generateDiagram(measures: Measure[]) {
    let measureDates: string[] = [];
    measures.forEach(function (item) {
      measureDates.push(item.measureDate);
    });
    let measureValues: number[] = [];
    measures.forEach(function (item) {
      measureValues.push(item.value);
    });

    console.log(measureDates);
    console.log(measureValues);

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
        ctx.fillRect(27, 10, chart.width - 39, chart.height - 39);
        ctx.restore();
      },
      defaults: {
        color: 'lightgray',
      },
    };

    const labels = [
      'Jan',
      'Mar',
      'Apr',
      'May',
      'Jun',
      'Jul',
      'Aug',
      'Sep',
      'Oct',
      'Nov',
      'Dec',
    ];
    new Chart('myChart', {
      type: 'scatter',
      data: {
        // labels: labels,
        datasets: [
          {
            backgroundColor: ['black'],
            borderColor: ['black'],
            borderWidth: 2,
            data: [
              { x: '2020/07/02', y: 15 },
              { x: '2020/11/03', y: 16 },
              { x: '2020/11/04', y: 19 },
              { x: '2020/11/07', y: 22 },
              { x: '2020/11/08', y: 23 },
              { x: '2021/08/09', y: 30 },
            ],
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
          tooltip: { enabled: true },
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
              tooltipFormat: 'dd/MM/yyyy',
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
