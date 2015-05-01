package stock;

import cmd.annotation.Option;
import com.google.inject.Inject;
import stock.meta.Stock;
import stock.name.XueQiuNameExtractor;

import java.io.PrintStream;
import java.util.List;

public class XueQiuNameCmd extends SubCmd{
    @Option(name = "-m", aliases = "--market-type", usage = "market class name", metaVar = "<market type>", required = true)
    private String market;
    @Inject
    private XueQiuNameExtractor extractor;

    @Override
    public void execute(PrintStream out) {
        System.out.println("run Xue Qiu Lister");
        try {
            List<Stock> allstocks=extractor.extract(market);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
