package remotefndload;

public class SshKey {
	private String filename;
	private String passphrase;

	public SshKey(String filename, String passphrase) {
		this.filename = filename;
		this.passphrase = passphrase;
	}

	public SshKey() {
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

}
