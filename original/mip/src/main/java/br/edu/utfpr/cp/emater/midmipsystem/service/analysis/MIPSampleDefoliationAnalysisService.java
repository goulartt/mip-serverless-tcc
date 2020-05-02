package br.edu.utfpr.cp.emater.midmipsystem.service.analysis;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.MIPSample;
import br.edu.utfpr.cp.emater.midmipsystem.service.mip.MIPSampleService;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.springframework.stereotype.Service;

@Service
public class MIPSampleDefoliationAnalysisService extends AbstractMIPSampleAnalysis {

    public MIPSampleDefoliationAnalysisService(MIPSampleService mipSampleService) {
        super(mipSampleService);
    }

    @Override
    public LineChartModel getChart() {
        var samples = this.readSamples();

        // To collect DAEAndDefoliation
        var DAEAndDefoliation = this.getDAEAndDefoliation(samples);

        // To generate map consolidating duplicated entries
        var DAEAndOccurrencesMap = consolidateDAEAndOccurrences(DAEAndDefoliation);

        // To create chart series by using the consolidated map
        var chartSeries = this.getChartSerie(DAEAndOccurrencesMap);

        var chartModel = new LineChartModel();

        // To add series to the chart
        chartModel.addSeries(chartSeries);

        // To set chart additional information
        this.setLineChartInfo(chartModel);

        return chartModel;
    }

    @Override
    public LineChartModel getChart(List<MIPSample> MIPSampleData) {

        var DAEAndDefoliation = this.getDAEAndDefoliation(MIPSampleData);

        var DAEAndOccurrencesMap = consolidateDAEAndOccurrences(DAEAndDefoliation);

        var chartSeries = this.getChartSerie(DAEAndOccurrencesMap);

        var chartModel = new LineChartModel();

        chartModel.addSeries(chartSeries);

        this.setLineChartInfo(chartModel);

        return chartModel;
    }

    List<DAEAndOccurrenceDTO> getDAEAndDefoliation(List<MIPSample> samples) {

        var result = samples.stream()
                .map(currentSample -> DAEAndOccurrenceDTO.builder().dae(currentSample.getDAE()).occurrence(currentSample.getDefoliation()).build())
                .collect(Collectors.toList());

        return result;
    }

    Map<Integer, Double> consolidateDAEAndOccurrences(List<DAEAndOccurrenceDTO> occurrences) {

        Comparator<DAEAndOccurrenceDTO> daeAndOccurrencesComparator = Comparator.comparingInt(DAEAndOccurrenceDTO::getDae);

        var result = occurrences.stream()
                .sorted(daeAndOccurrencesComparator)
                .collect(
                        Collectors.groupingBy(
                                DAEAndOccurrenceDTO::getDae, 
                                Collectors.averagingDouble(DAEAndOccurrenceDTO::getOccurrence)
                        )
                );
        
        
        return result;
    }

    LineChartSeries getChartSerie(Map<Integer, Double> occurrencesGroupped) {

        var result = new LineChartSeries("Desfolha");

        occurrencesGroupped.keySet().forEach(currentDAE -> {
            result.set(currentDAE, occurrencesGroupped.get(currentDAE));
        });

        return result;
    }

    void setLineChartInfo(LineChartModel aChartModel) {

        aChartModel.setLegendPosition("nw");
        aChartModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

        aChartModel.setZoom(true);
        aChartModel.setAnimate(true);

        Axis xAxis = aChartModel.getAxis(AxisType.X);
        xAxis.setLabel("Dias Após Emergência");
        xAxis.setMin(0);

        Axis yAxis = aChartModel.getAxis(AxisType.Y);
        yAxis.setLabel("(%) Desfolha");
        yAxis.setMin(0);
    }

}
