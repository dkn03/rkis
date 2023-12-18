package rkis_8;

/**Класс, реализующий сущность парфюмерии*/
public class  Perfumery {

    private int id;
    private String type;
    private String color;
    private String aroma;
    private int volume;
    private double concentration;

    public Perfumery(){
        this.id = 0;
        this.type = null;
        this.color = null;
        this.aroma = null;
        this.volume = 0;
        this.concentration = 0;
    }

    public Perfumery(String type, String color, String aroma, int volume, double concentration) {
        setId(0);
        setType(type);
        setColor(color);
        setAroma(aroma);
        setVolume(volume);
        setConcentration(concentration);
    }

    public Perfumery(int id, String type, String color, String aroma, int volume, double concentration) {
        setId(id);
        setType(type);
        setColor(color);
        setAroma(aroma);
        setVolume(volume);
        setConcentration(concentration);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAroma() {
        return aroma;
    }

    public void setAroma(String aroma) {
        this.aroma = aroma;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        if (volume < 0){
            throw new IllegalArgumentException("Value must be > 0");
        }
        this.volume = volume;
    }

    public double getConcentration() {
        return concentration;
    }

    public void setConcentration(double concentration) {
        if (0 > concentration || 1 < concentration){
            throw new IllegalArgumentException("Value must be between 0 and 1");
        }
        this.concentration = concentration;
    }



    @Override
    public String toString() {
        return "Perfumery{id = '" + id + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", aroma='" + aroma + '\'' +
                ", volume=" + volume +
                ", concentration=" + concentration +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
