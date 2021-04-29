package com.redspeaks.gang.api.gangs;

import java.util.List;

public interface GangBase {

    LeaderBoard getLeaderBoard();

    double getGoalExp(int level);

    List<String> getMembers();

    void addMember(GangPlayer gangPlayer);

    void kickMember(GangPlayer gangPlayer);

    boolean isMember(GangPlayer gangPlayer);

    boolean isLevelExist(int level);
}
