package br.edu.utfpr.cp.emater.midmipsystem.service.analysis;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.Pest;
import br.edu.utfpr.cp.emater.midmipsystem.service.mip.MIPSampleService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MIPSampleBedBugPestAnalysisService extends AbstractMIPSamplePestAnalysis {

    public MIPSampleBedBugPestAnalysisService(MIPSampleService mipSampleService) {
        super(mipSampleService);
    }

    protected List<Pest> getPests() {
        return List.of(
                this.getMipSampleService().readPestById(9L).get(),
                this.getMipSampleService().readPestById(10L).get(),
                this.getMipSampleService().readPestById(11L).get(),
                this.getMipSampleService().readPestById(12L).get(),
                this.getMipSampleService().readPestById(13L).get(),
                this.getMipSampleService().readPestById(14L).get(),
                this.getMipSampleService().readPestById(15L).get(),
                this.getMipSampleService().readPestById(16L).get(),
                this.getMipSampleService().readPestById(17L).get(),
                this.getMipSampleService().readPestById(18L).get());
    }
}
