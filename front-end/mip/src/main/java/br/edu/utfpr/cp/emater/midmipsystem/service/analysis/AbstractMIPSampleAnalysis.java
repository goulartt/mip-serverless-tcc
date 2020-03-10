package br.edu.utfpr.cp.emater.midmipsystem.service.analysis;

import java.util.List;
import java.util.Optional;

import org.primefaces.model.chart.LineChartModel;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.MacroRegion;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Region;
import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.MIPSample;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUser;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.service.mip.MIPSampleService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractMIPSampleAnalysis {

    @Getter(AccessLevel.PROTECTED)
    private final MIPSampleService mipSampleService;

    public abstract LineChartModel getChart();

    public abstract LineChartModel getChart(List<MIPSample> MIPSampleData);

    public List<Region> readRegionsAvailableByMacroRegionId(Long selectedMacroRegionId) {
        return this.mipSampleService.readAllRegionsFor(selectedMacroRegionId);
    }

    public List<MacroRegion> readAllMacroRegionsWithSurvey() {
        return this.mipSampleService.readAllMacroRegionsWithSurvey();
    }

    public List<City> readCitiesAvailableByRegionId(Long aRegionId) {
        try {
            return this.mipSampleService.readAllCitiesByRegionId(aRegionId);

        } catch (EntityNotFoundException ex) {
            return null;
        }
    }

    public List<Field> readURsAvailableByCityId(Long aCityId) {
        return this.mipSampleService.readAllFieldsByCityId(aCityId);
    }

    public List<MIPSample> readSamples() {
        return mipSampleService.readAll();
    }

    public List<MIPSample> readMIPSamplesByMacroRegionId(Long aMacroRegionId) {
        return mipSampleService.readByMacroRegionId(aMacroRegionId);
    }

    public List<MIPSample> readMIPSamplesByRegionId(Long aRegionId) {
        return mipSampleService.readByRegionId(aRegionId);
    }

    public List<MIPSample> readMIPSamplesByCityId(Long aCityId) {
        return mipSampleService.readByCityId(aCityId);
    }

    public List<MIPSample> readMIPSamplesByURId(Long anURId) {
        return mipSampleService.readByURId(anURId);
    }

    public List<MIPSample> readMIPSamplesByMIPUser(MIPUser aMIPUser) {
        return mipSampleService.readByMIPUser(aMIPUser);
    }

    public Optional<MacroRegion> readMacroRegionById(Long aMacroRegionId) {
        return mipSampleService.readyByMacroRegionId(aMacroRegionId);
    }

    public Optional<Region> readRegionById(Long aRegionId) {
        return mipSampleService.readyRegionById(aRegionId);
    }

    public Optional<City> readCityById(Long aCityId) {
        return mipSampleService.readyCityById(aCityId);
    }

    public Optional<Field> readFieldById(Long aFieldId) {
        return mipSampleService.readFieldById(aFieldId);
    }

}
