package remotefndload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NlsLang
{
  private String oracleEncoding;
  private String javaEncoding;
  private Boolean enabled;

  @Override
  public String toString() {
    return oracleEncoding;
  }
}
