package br.edu.utfpr.cp.emater.midmipsystem.service.analysis;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.MIPSample;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.Pest;
import br.edu.utfpr.cp.emater.midmipsystem.service.mip.MIPSampleService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

public abstract class AbstractMIPSamplePestAnalysis extends AbstractMIPSampleAnalysis {

    public AbstractMIPSamplePestAnalysis(MIPSampleService mipSampleService) {
        super(mipSampleService);
    }

    public LineChartModel getChart(List<MIPSample> MIPSampleData) {
        var pests = this.getPests();

        var pestAndDAEAndOccurrences = getDAEAndOccurrences(pests, MIPSampleData);

        var pestAndDAEAndOccurrencesMap = consolidateDAEAndOccurrences(pestAndDAEAndOccurrences);

        var chartSeries = getChartSeries(pestAndDAEAndOccurrencesMap);

        var chartModel = new LineChartModel();
        chartSeries.forEach(chartModel::addSeries);
        this.setLineChartInfo(chartModel);

        return chartModel;
    }

    public LineChartModel getChart() {
        var samples = this.readSamples();

        var pests = this.getPests();

        var pestAndDAEAndOccurrences = getDAEAndOccurrences(pests, samples);

        var pestAndDAEAndOccurrencesMap = consolidateDAEAndOccurrences(pestAndDAEAndOccurrences);

        var chartSeries = getChartSeries(pestAndDAEAndOccurrencesMap);

        var chartModel = new LineChartModel();
        chartSeries.forEach(chartModel::addSeries);
        this.setLineChartInfo(chartModel);

        return chartModel;
    }

    protected abstract List<Pest> getPests();

    Map<Pest, List<DAEAndOccurrenceDTO>> getDAEAndOccurrences(List<Pest> pests, List<MIPSample> samples) {

        var result = new HashMap<Pest, List<DAEAndOccurrenceDTO>>();

        pests.forEach(currentPest
                -> result.put(currentPest,
                        samples.stream()
                                .filter(currentSample -> currentSample.getDAEAndPestOccurrenceByPest(currentPest).isPresent())
                                .map(currentSample -> currentSample.getDAEAndPestOccurrenceByPest(currentPest).get())
                                .collect(Collectors.toList())
                )
        );

        return result;
    }

    Map<Pest, Map<Integer, Double>> consolidateDAEAndOccurrences(Map<Pest, List<DAEAndOccurrenceDTO>> occurrences) {

        var result = new HashMap<Pest, Map<Integer, Double>>();

        occurrences.keySet().forEach(currentOccurrence -> {

            result.put(currentOccurrence,
                    occurrences.get(currentOccurrence).stream()
                            .collect(
                                    Collectors.groupingBy(
                                            DAEAndOccurrenceDTO::getDae,
                                            Collectors.averagingDouble(DAEAndOccurrenceDTO::getOccurrence)
                                    )
                            )
            );
        });

        return result;
    }

    List<LineChartSeries> getChartSeries(Map<Pest, Map<Integer, Double>> occurrencesGrouppedByPest) {

        var result = new ArrayList<LineChartSeries>();

        occurrencesGrouppedByPest.keySet().stream().forEach(currentPest -> {

            var currentSerie = new LineChartSeries(currentPest.getDescription());

            var currentPestOccurrence = occurrencesGrouppedByPest.get(currentPest);

            currentPestOccurrence.keySet().forEach(currentDAE -> {
                currentSerie.set(currentDAE, currentPestOccurrence.get(currentDAE));
            });

            result.add(currentSerie);

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
        yAxis.setLabel("No. Insetos/metro");
        yAxis.setTickFormat("%#.2f");
        yAxis.setMin(0);
    }

}
