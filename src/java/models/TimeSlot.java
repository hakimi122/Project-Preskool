package models;

public class TimeSlot {
    private int slotId;
    private String description;

    public TimeSlot() {
    }

    public TimeSlot(int slotId, String description) {
        this.slotId = slotId;
        this.description = description;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
