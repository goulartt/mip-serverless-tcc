package br.edu.utfpr.cp.emater.midmipsystem.service.analysis;

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
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.MIPSample;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.PestNaturalPredator;
import br.edu.utfpr.cp.emater.midmipsystem.service.mip.MIPSampleService;

@Service
public class MIPSamplePredatorAnalysisService extends AbstractMIPSampleAnalysis {

    public MIPSamplePredatorAnalysisService(MIPSampleService mipSampleService) {
        super(mipSampleService);
    }

    public LineChartModel getChart(List<MIPSample> MIPSampleData) {

        var predators = this.getPredators();

        var pestAndDAEAndOccurrences = getDAEAndOccurrences(predators, MIPSampleData);

        var pestAndDAEAndOccurrencesMap = consolidateDAEAndOccurrences(pestAndDAEAndOccurrences);

        var chartSeries = getChartSeries(pestAndDAEAndOccurrencesMap);

        var chartModel = new LineChartModel();
        chartSeries.forEach(chartModel::addSeries);
        this.setLineChartInfo(chartModel);

        return chartModel;

    }

    public LineChartModel getChart() {
        var samples = this.readSamples();

        var predators = this.getPredators();

        var pestAndDAEAndOccurrences = getDAEAndOccurrences(predators, samples);

        var pestAndDAEAndOccurrencesMap = consolidateDAEAndOccurrences(pestAndDAEAndOccurrences);

        var chartSeries = getChartSeries(pestAndDAEAndOccurrencesMap);

        var chartModel = new LineChartModel();
        chartSeries.forEach(chartModel::addSeries);
        this.setLineChartInfo(chartModel);

        return chartModel;
    }

    Map<PestNaturalPredator, List<DAEAndOccurrenceDTO>> getDAEAndOccurrences(List<PestNaturalPredator> predators, List<MIPSample> samples) {

        var result = new HashMap<PestNaturalPredator, List<DAEAndOccurrenceDTO>>();

        predators.forEach(currentPredator
                -> result.put(currentPredator,
                        samples.stream()
                                .filter(currentSample -> currentSample.getDAEAndPredatorOccurrenceByPredator(currentPredator).isPresent())
                                .map(currentSample -> currentSample.getDAEAndPredatorOccurrenceByPredator(currentPredator).get())
                                .collect(Collectors.toList())
                )
        );

        return result;
    }

    Map<PestNaturalPredator, Map<Integer, Double>> consolidateDAEAndOccurrences(Map<PestNaturalPredator, List<DAEAndOccurrenceDTO>> occurrences) {

        var result = new HashMap<PestNaturalPredator, Map<Integer, Double>>();

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

    List<LineChartSeries> getChartSeries(Map<PestNaturalPredator, Map<Integer, Double>> occurrencesGrouppedByPest) {

        var result = new ArrayList<LineChartSeries>();

        occurrencesGrouppedByPest.keySet().stream().forEach(currentPredator -> {

            var currentSerie = new LineChartSeries(currentPredator.getDescription());

            var currentPestOccurrence = occurrencesGrouppedByPest.get(currentPredator);

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

    protected List<PestNaturalPredator> getPredators() {
        return List.of(
                this.getMipSampleService().readPredatorById(1L).get(),
                this.getMipSampleService().readPredatorById(2L).get(),
                this.getMipSampleService().readPredatorById(3L).get(),
                this.getMipSampleService().readPredatorById(4L).get(),
                this.getMipSampleService().readPredatorById(5L).get(),
                this.getMipSampleService().readPredatorById(6L).get(),
                this.getMipSampleService().readPredatorById(7L).get(),
                this.getMipSampleService().readPredatorById(8L).get(),
                this.getMipSampleService().readPredatorById(9L).get(),
                this.getMipSampleService().readPredatorById(10L).get(),
                this.getMipSampleService().readPredatorById(11L).get(),
                this.getMipSampleService().readPredatorById(12L).get());

    }

}
