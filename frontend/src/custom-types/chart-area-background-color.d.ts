import { ChartType } from 'chart.js';

declare module 'chart.js' {
  interface PluginOptionsByType<TType extends ChartType> {
    chart_area_background_color?: {
      color?: string;
    };
  }
}
