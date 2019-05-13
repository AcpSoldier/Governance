package vision.thomas.government.accounts;

import java.util.UUID;

public class Account {

    private final int id;

    private final UUID uuid;

    private int respect;

    public Account(int id, UUID uuid, int respect) {

        this.id = id;
        this.uuid = uuid;
        this.respect = respect;
    }

    public int getId() {

        return id;
    }

    public UUID getUniqueId() {

        return uuid;
    }

    public int getRespect() {

        return respect;
    }

    protected void incrementRespect(int respect) {

        this.respect += respect;
    }

}
