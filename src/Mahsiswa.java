public class Mahsiswa {
    private String nim;
    private String nama;
    private String fk;
    private String jr;
    private String alamat;
    private String jk;

    public Mahsiswa(String nim, String nama, String fk, String jr, String alamat,
    String jk){
        this.nim = nim;
        this.nama = nama;
        this.fk = fk;
        this.jr = jr;
        this.alamat = alamat;
        this.jk = jk;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFk() {
        return fk;
    }

    public void setFk(String fk) {
        this.fk = fk;
    }

    public String getJr() {
        return jr;
    }

    public void setJr(String jr) {
        this.jr = jr;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }
}
