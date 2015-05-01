package stock;

import com.google.inject.Inject;
import stock.meta.Stock;
import stock.name.XueQiuNameExtractor;

import java.io.PrintStream;
import java.util.List;

public class XueQiuNameCmd extends SubCmd{
    @Inject
    private XueQiuNameExtractor extractor;

    @Override
    public void execute(PrintStream out) {
        System.out.println("run Xue Qiu Lister");
        List<Stock> allstocks=extractor.extract();
    }
}
