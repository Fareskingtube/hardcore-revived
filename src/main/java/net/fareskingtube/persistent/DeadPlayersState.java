package net.fareskingtube.persistent;

import com.mojang.authlib.GameProfile;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeadPlayersState extends PersistentState {
    private final List<GameProfile> deadPlayers = new ArrayList<>();


    public static final PersistentState.Type<DeadPlayersState> TYPE = new PersistentState.Type<>(
            DeadPlayersState::new,
            DeadPlayersState::createFromNbt,
            null
    );


    public boolean isDead(UUID uuid) {
        return deadPlayers.stream().anyMatch(p -> p.getId().equals(uuid));
    }

    public void addDeadPlayer(GameProfile profile) {
        if (!isDead(profile.getId())) {
            deadPlayers.add(profile);
            markDirty();
        }
    }

    public void removeDeadPlayer(UUID uuid) {
        if (deadPlayers.removeIf(p -> p.getId().equals(uuid))) {
            markDirty();
        }
    }

    public List<GameProfile> getDeadPlayers() {
        return deadPlayers;
    }


    private NbtCompound profileToNbt(GameProfile profile) {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("Id", profile.getId());
        if (profile.getName() != null) {
            nbt.putString("Name", profile.getName());
        }
        return nbt;
    }

    private GameProfile profileFromNbt(NbtCompound nbt) {
        UUID id = nbt.getUuid("Id");
        String name = nbt.contains("Name") ? nbt.getString("Name") : "";
        return new GameProfile(id, name);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();
        for (GameProfile profile : deadPlayers) {
            list.add(profileToNbt(profile));
        }
        nbt.put("DeadPlayers", list);
        return nbt;
    }

    public static DeadPlayersState createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        DeadPlayersState state = new DeadPlayersState();
        NbtList list = nbt.getList("DeadPlayers", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : list) {
            state.deadPlayers.add(state.profileFromNbt((NbtCompound) element));
        }
        return state;
    }

    public static DeadPlayersState get(MinecraftServer server) {
        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        return manager.getOrCreate(TYPE, HardcoreRevived.MOD_ID + "_dead_players");
    }
}
