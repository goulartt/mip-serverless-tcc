package br.edu.utfpr.cp.emater.midmipsystem.service.analysis;

import br.edu.utfpr.cp.emater.midmipsystem.entity.mip.Pest;
import br.edu.utfpr.cp.emater.midmipsystem.service.mip.MIPSampleService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MIPSampleCaterpillarPestAnalysisService extends AbstractMIPSamplePestAnalysis {

    public MIPSampleCaterpillarPestAnalysisService(MIPSampleService mipSampleService) {
        super(mipSampleService);
    }

    protected List<Pest> getPests() {
        return List.of(
                this.getMipSampleService().readPestById(1L).get(),
                this.getMipSampleService().readPestById(2L).get(),
                this.getMipSampleService().readPestById(3L).get(),
                this.getMipSampleService().readPestById(4L).get(),
                this.getMipSampleService().readPestById(5L).get(),
                this.getMipSampleService().readPestById(6L).get(),
                this.getMipSampleService().readPestById(7L).get(),
                this.getMipSampleService().readPestById(8L).get());
    }
}
