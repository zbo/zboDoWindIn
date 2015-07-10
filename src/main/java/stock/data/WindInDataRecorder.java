package stock.data;

import org.apache.commons.io.FileUtils;
import stock.meta.MatchedStock;
import stock.meta.WindIn;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;

/**
 * Created by bob.zhu on 15-5-2.
 */
public class WindInDataRecorder {
    File file;
    public void record(MatchedStock matchedStockck) throws IOException {
        System.out.println("recoding "+matchedStockck.getSotck().getName());
        file=WindIn.getSqfile();
        recordBasicInfo(matchedStockck);
        recordSB(matchedStockck);
        recordQFii(matchedStockck);
        recordImages(matchedStockck);

    }

    private void recordImages(MatchedStock matchedStockck) throws IOException {
        String day_image=WindIn.getSina_day_img(matchedStockck.getSotck().getCode().toLowerCase());
        String week_image=WindIn.getSina_week_img(matchedStockck.getSotck().getCode().toLowerCase());
        String image_format="<img src=%s>";
        FileUtils.write(file,format(image_format,day_image)+"\n","UTF-8",true);
        FileUtils.write(file,format(image_format,week_image)+"<br>\n","UTF-8",true);
    }

    private void recordSB(MatchedStock matchedStockck) throws IOException {

        String sbThisQ = matchedStockck.getSbThisQ()!=null?matchedStockck.getSbThisQ():"没有数据";
        FileUtils.write(file, sbThisQ +"<br>\n","UTF-8",true);
        String sbLastQ = matchedStockck.getSbLastQ()!=null?matchedStockck.getSbLastQ():"没有数据";
        FileUtils.write(file, sbLastQ +"<br>\n","UTF-8",true);
    }
    private void recordQFii(MatchedStock matchedStockck) throws IOException {
        String qfiiThisQ = matchedStockck.getQfiiThisQ()!=null?matchedStockck.getQfiiThisQ():"没有数据";
        FileUtils.write(file, qfiiThisQ +"<br>\n","UTF-8",true);
        String qfiiLastQ = matchedStockck.getQfiiLastQ()!=null?matchedStockck.getQfiiLastQ():"没有数据";
        FileUtils.write(file, qfiiLastQ +"<br>\n","UTF-8",true);
    }

    private void recordBasicInfo(MatchedStock matchedStockck) throws IOException {
        String marketCode = matchedStockck.getSotck().getMarketCode();
        String marketShortName = matchedStockck.getSotck().getMarketShortName();
        String jbm1_content=format(WindIn.getSource_base(), marketCode, marketShortName);
        String s_jbm1 = "<a href=%s>========%s========</a><br>\n";
        FileUtils.write(file,format(s_jbm1,jbm1_content,matchedStockck.getSotck().getName()),"UTF-8",true);
        String jbm2_content=format(WindIn.getBasic_base(),marketCode,marketShortName);
        String s_jbm2="<a href=%s>基本面</a><br>\n";
        FileUtils.write(file,format(s_jbm2,jbm2_content),"UTF-8",true);
    }
}
