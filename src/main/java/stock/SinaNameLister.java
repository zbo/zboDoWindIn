package stock;

import com.google.inject.Inject;
import stock.meta.Stock;
import stock.name.SinaNameExtractor;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by twer on 15-4-30.
 */
public class SinaNameLister  extends SubCmd{
    @Inject
    private SinaNameExtractor extractor;

    @Override
    public void execute(PrintStream out) {
        System.out.println("run SinaName Lister");
        List<Stock> allstocks=extractor.extract();
    }
}
