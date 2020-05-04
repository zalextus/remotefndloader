package remotefndload;

public class NlsLang
{
  private String oracleEncoding;
  private String javaEncoding;

  public NlsLang()
  {
  }

  public NlsLang(String oracleEncoding, String javaEncoding)
  {
    this.oracleEncoding = oracleEncoding;
    this.javaEncoding = javaEncoding;
  }

  public void setOracleEncoding(String oracleEncoding)
  {
    this.oracleEncoding = oracleEncoding;
  }

  public String getOracleEncoding()
  {
    return oracleEncoding;
  }

  public void setJavaEncoding(String javaEncoding)
  {
    this.javaEncoding = javaEncoding;
  }

  public String getJavaEncoding()
  {
    return javaEncoding;
  }
  
  public String toString() 
  {
    return oracleEncoding;
  }
}
