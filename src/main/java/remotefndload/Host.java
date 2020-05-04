package remotefndload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class Host implements Comparable {

    private String prompt;
    private String hostname;
    private String dbUser;
    private String dbPassword;
    private String osUser;
    private String osPassword;
    private String osdirname;
    private String envcmd;
    private String command;
    private String remoteTempCommandFileName;

    public int compareTo(Object o) {
        Host hostOther = (Host)o;
        return prompt.compareTo(hostOther.getPrompt());
    }

    @Override
    public String toString() {
        return prompt;
    }
}
