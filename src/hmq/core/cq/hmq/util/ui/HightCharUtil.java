package core.cq.hmq.util.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.cq.hmq.modal.HighChartsColumnModal;
import core.cq.hmq.modal.HighChartsPieModal;


/**
 * hightchar 工具类
 * 
 * @author 何建
 * 
 */
public class HightCharUtil {

	/**
	 * 
	 * @param divId
	 *            容器id
	 * @param title
	 *            标题
	 * @param xAis
	 *            x坐标显示{ "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月" };
	 * @param yAisText
	 *            y坐标标题
	 * @param list
	 *            数据List<HighChartsColumnModal>
	 * @return
	 */
	public static Map<String, Object> column(String divId, String title,
			String[] xAis, String yAisText, List<HighChartsColumnModal> list) {
		Map<String, Object> mapCOlumn = new HashMap<String, Object>();
		// (1)chart
		Map<String, String> mapChart = new HashMap<String, String>();
		mapChart.put("renderTo", divId);
		mapChart.put("type", "column");
		// (2)title
		Map<String, String> mapTile = new HashMap<String, String>();
		mapTile.put("text", title);
		// (3)x坐标显示
		Map<String, String[]> mapXAis = new HashMap<String, String[]>();
		// String[] xAis = { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月" };
		mapXAis.put("categories", xAis);
		// (4)y坐标
		Map<String, Object> mapYAis = new HashMap<String, Object>();
		mapYAis.put("min", 0);
		Map<String, String> mapYAisTitle = new HashMap<String, String>();
		mapYAisTitle.put("text", yAisText);
		mapYAis.put("title", mapYAisTitle);
		// (5)tooltip
		Map<String, Object> mapTooltip = new HashMap<String, Object>();
		mapTooltip.put("headerFormat",
				"<span style='font-size:14px'>{point.key}</span><table>");
		mapTooltip
				.put(
						"pointFormat",
						"<tr><td style='color:{series.color};padding:0'>{series.name}: </td><td style='padding:0'><b>{point.y:.1f}个</b></td></tr>");
		mapTooltip.put("footerFormat", "</table>");
		mapTooltip.put("shared", true);
		mapTooltip.put("useHTML", true);
		// (6)plotOptions
		Map<String, Object> mapPlotOptions = new HashMap<String, Object>();
		Map<String, Object> mapPlotOptionsColumn = new HashMap<String, Object>();
		mapPlotOptionsColumn.put("pointPadding", 0.2);
		mapPlotOptionsColumn.put("borderWidth", 0);
		mapPlotOptions.put("column", mapPlotOptionsColumn);

		mapCOlumn.put("chart", mapChart);
		mapCOlumn.put("title", mapTile);
		mapCOlumn.put("xAxis", mapXAis);
		mapCOlumn.put("yAxis", mapYAis);
		mapCOlumn.put("tooltip", mapTooltip);
		mapCOlumn.put("plotOptions", mapPlotOptions);
		mapCOlumn.put("series", list);
		return mapCOlumn;
	}

	/**
	 * 
	 * @param divId
	 *            容器id
	 * @param title
	 *            标题
	 * @param list
	 *            数据
	 * @return
	 */
	public static Map<String, Object> pie(String divId, String title,
			List<HighChartsPieModal> list) {
		Map<String, Object> mapPie = new HashMap<String, Object>();
		// (1)chart
		Map<String, Object> mapChart = new HashMap<String, Object>();
		mapChart.put("renderTo", divId);
		mapChart.put("plotBackgroundColor", null);
		mapChart.put("plotBorderWidth", null);
		mapChart.put("plotShadow", false);
		// (2)title
		Map<String, String> mapTile = new HashMap<String, String>();
		mapTile.put("text", title);
		// (3)tooltip
		Map<String, Object> mapTooltip = new HashMap<String, Object>();
		mapTooltip.put("pointFormat",
				"{series.name}: <b>{point.percentage:.1f}%</b>");
		// (3)plotOptions
		Map<String, Object> mapPlotOptions = new HashMap<String, Object>();
		Map<String, Object> mapPlotOptionsPie = new HashMap<String, Object>();
		mapPlotOptionsPie.put("allowPointSelect", true);
		mapPlotOptionsPie.put("cursor", "pointer");
		Map<String, Object> mapPlotOptionsPieDataLabels = new HashMap<String, Object>();
		mapPlotOptionsPieDataLabels.put("enabled", true);
		mapPlotOptionsPieDataLabels.put("color", "#000000");
		mapPlotOptionsPieDataLabels.put("connectorColor", "#000000");
		mapPlotOptionsPieDataLabels.put("format",
				"<b>{point.name}</b>:{point.percentage:.1f} %");
		mapPlotOptionsPie.put("dataLabels", mapPlotOptionsPieDataLabels);
		mapPlotOptions.put("pie", mapPlotOptionsPie);
		
		
		Map<String, Object> mapSeries = new HashMap<String, Object>();
		mapSeries.put("type", "pie");
		mapSeries.put("name", "xxxxx");
		mapSeries.put("data", list);

		mapPie.put("chart", mapChart);
		mapPie.put("title", mapTile);
		mapPie.put("tooltip", mapTooltip);
		mapPie.put("plotOptions", mapPlotOptions);
		mapPie.put("series", mapSeries);
		return mapPie;

	}

}
