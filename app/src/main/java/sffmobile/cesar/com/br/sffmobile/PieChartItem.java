package sffmobile.cesar.com.br.sffmobile;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing.EasingOption;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.List;

import org.xmlpull.v1.XmlPullParser;

public class PieChartItem extends ChartItem {
	private boolean calcSaldo;
	private List<Double> pieChartValues;

	private static class ViewHolder {
		PieChart chart;
		TextView chartTitle;
		List<String> legendLabels;
		ViewGroup legends;

		private ViewHolder() {
		}
	}

	private static class ChartValueSelectedListener implements
			OnChartValueSelectedListener {
		private Context context;
		private List<String> legendLabels;
		private List<Double> pieChartValues;

		public ChartValueSelectedListener(Context context,
				List<String> legendLabels, List<Double> pieChartValues) {
			this.context = context;
			this.legendLabels = legendLabels;
			this.pieChartValues = pieChartValues;
		}

		@Override
		public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
			Toast.makeText(
					this.context,
					this.legendLabels.get(e.getXIndex())
							+ " - "
							+ SFFUtil.getFormattedNumber(this.pieChartValues
									.get(e.getXIndex())), Toast.LENGTH_SHORT).show();
		}

		public void onNothingSelected() {

		}

	}

	public PieChartItem(ChartData<?> cd, Context context,
			List<Double> pieChartValues) {
		super(cd);
		this.pieChartValues = pieChartValues;
	}

	public int getItemType() {
		return TYPE_PIECHART;
	}

	public View getView(int position, View convertView, Context c) {

		ViewHolder holder;

		if (convertView == null) {

			holder = new ViewHolder();

			convertView = LayoutInflater.from(c).inflate(
					R.layout.list_item_piechart, null);

			holder.chart = (PieChart) convertView.findViewById(R.id.chart);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		holder.chartTitle = (TextView) convertView
				.findViewById(R.id.char_title);

		holder.legends = (ViewGroup) convertView
				.findViewById(R.id.chart_legends);

		holder.legends.removeAllViews();

		holder.legendLabels = this.mChartData.getXVals();

		int[] legend_color = this.mChartData.getColors();

		for (int i = 0; i < holder.legendLabels.size(); i++) {

			View legendItem = LayoutInflater.from(c).inflate(
					R.layout.list_item_chart_legends, null);

			legendItem.findViewById(R.id.char_legend_color_container)
					.setBackgroundColor(legend_color[i]);

			((TextView) legendItem.findViewById(R.id.char_legend_label))
					.setText(holder.legendLabels.get(i)
							+ " - R$ "
							+ SFFUtil.getFormattedNumber(this.pieChartValues
									.get(i)));

			holder.legends.addView(legendItem);

			
		}
		
		if (isCalcSaldo()) {
			View legendItem = LayoutInflater.from(c).inflate(
					R.layout.list_item_chart_legends, null);
			
			legendItem.findViewById(R.id.char_legend_color_container)
					.setBackgroundColor(Color.GREEN);
			
			((TextView) legendItem.findViewById(R.id.char_legend_label))
					.setText("Saldo Restante - R$"
							+ SFFUtil.getFormattedNumber(this.pieChartValues.get(1) -  this.pieChartValues.get(0)));
			
			holder.legends.addView(legendItem);
		}

		holder.chart.setDescription("");
		holder.chart.setDrawHoleEnabled(false);
		holder.chart.setUsePercentValues(true);
		holder.chart.setRotationEnabled(false);
		holder.chartTitle.setText(this.mChartData.getDataSetByIndex(0)
				.getLabel());
		holder.chart
				.setOnChartValueSelectedListener(new ChartValueSelectedListener(
						c, holder.legendLabels, this.pieChartValues));
		holder.chart.setDrawSliceText(false);
		holder.chart.setData((PieData) this.mChartData);
		holder.chart.getLegend().setEnabled(false);
		holder.chart.animateY(1500, EasingOption.EaseInOutQuad);
		return convertView;
	}

	public boolean isCalcSaldo() {
		return this.calcSaldo;
	}

	public void setCalcSaldo(boolean calcSaldo) {
		this.calcSaldo = calcSaldo;
	}
}
