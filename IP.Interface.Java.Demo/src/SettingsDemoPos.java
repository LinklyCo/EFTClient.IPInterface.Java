import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import java.io.*;

public class SettingsDemoPos {
    public SettingsDemoPos() {}

    public String HostAddress = "127.0.0.1";
    public String getHostAddress() {
        return HostAddress;
    }
    public void setHostAddress(String hostAddress) {
        HostAddress = hostAddress;
    }

    public int HostPort = 2011;
    public int getHostPort() {
        return HostPort;
    }
    public void setHostPort(int hostPort) {
        HostPort = hostPort;
    }

    public boolean UseSSL = false;
    public boolean isUseSSL() {
        return UseSSL;
    }
    public void setUseSSL(boolean useSSL) {
        UseSSL = useSSL;
    }

    public String CloudUsername = "";
    public String getCloudUsername() {
        return CloudUsername;
    }
    public void setCloudUsername(String cloudUsername) {
        CloudUsername = cloudUsername;
    }

    public String CloudPassword = "";
    public String getCloudPassword() {
        return CloudPassword; }
    public void setCloudPassword(String cloudPassword) {
        CloudPassword = cloudPassword;
    }

    public String CloudToken = "";
    public String getCloudToken() {
        return CloudToken;
    }
    public void setCloudToken(String cloudToken) {
        CloudToken = cloudToken;
    }

    public void SaveSettings(){
        org.json.simple.JSONObject _settingsJSON = new org.json.simple.JSONObject();
        _settingsJSON.put("hostAddress", getHostAddress());
        _settingsJSON.put("hostPort", getHostPort());
        _settingsJSON.put("useSSL", isUseSSL());
        _settingsJSON.put("cloudUsername", getCloudUsername());
        _settingsJSON.put("cloudPassword",getCloudPassword());
        _settingsJSON.put("cloudToken", getCloudToken());

        try {
            FileOutputStream f = new FileOutputStream(System.getenv("LOCALAPPDATA") + "\\PC-EFTPOS\\javademopos.json", false);
            f.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter file = new FileWriter(System.getenv("LOCALAPPDATA") + "\\PC-EFTPOS\\javademopos.json")) {
            file.write(_settingsJSON.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadSettings(){

        JSONParser parser = new JSONParser();
        try (FileReader file = new FileReader(System.getenv("LOCALAPPDATA") + "\\PC-EFTPOS\\javademopos.json")) {
            Object obj = parser.parse(file);
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

            setHostAddress((String) jsonObject.get(("hostAddress")));
            setHostPort(((Long)jsonObject.get("hostPort")).intValue());
            setUseSSL((boolean)jsonObject.get("useSSL"));

            setCloudUsername((String) jsonObject.get("cloudUsername"));
            setCloudPassword((String) jsonObject.get("cloudPassword"));
            setCloudToken((String) jsonObject.get("cloudToken"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exc)
        {
            exc.printStackTrace();
        }

    }

}
