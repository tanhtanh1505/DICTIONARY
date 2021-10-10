import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InitData {
    private static HashMap<String, String> EVdata = new HashMap<>();
    private static ArrayList<String> EVkeys = new ArrayList<>();

    private static HashMap<String, String> VEdata = new HashMap<>();
    private static ArrayList<String> VEkeys = new ArrayList<>();

    //Gọi hàm này đầu tiên để load dữ liệu vào các Map và Array
    public static void loadData(){
        load("./data/E_V.zip", EVdata, EVkeys);
        load("./data/V_E.zip", VEdata, VEkeys);
    }

    //Hàm load, ko cần gọi
    public static void load(String path, HashMap<String, String> Tdata, ArrayList<String> Tkeys){
        try{
            FileInputStream file = new FileInputStream(path);
            ZipInputStream zipStream = new ZipInputStream(file);
            ZipEntry entry = zipStream.getNextEntry();

            BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream,"utf-8"));

            String line, word, meaning;
            int wordsNum = 0;
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf("<html>");
                if (index != -1) {
                    word = line.substring(0, index);
                    word = word.trim();
                    Tkeys.add(word);
                    meaning = line.substring(index);
                    Tdata.put(word, meaning);
                    wordsNum++;
                }
            }
            reader.close();
            System.out.println("Number Words from " + path + " : " + wordsNum );
        }
        catch (Exception e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText(e.getMessage());
            error.show();
        }

    }

    //Trả về Map<từ, nghĩa> E-V
    public static HashMap<String, String> getHashMapEV(){
        return (HashMap<String, String>) EVdata.clone();
    }
    //Trả về mảng từ tiếng anh
    public static ArrayList<String> getKeyEV(){ return (ArrayList<String>) EVkeys.clone(); }

    //Trả về Map<từ, nghĩa> V-E
    public static HashMap<String, String> getHashMapVE(){
        return (HashMap<String, String>) VEdata.clone();
    }

    //Trả về mảng từ tiếng việt
    public static ArrayList<String> getKeyVE(){ return (ArrayList<String>) VEkeys.clone(); }
}
