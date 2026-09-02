package net.fareskingtube.persistent;

import com.mojang.authlib.GameProfile;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RevivalQueueState extends PersistentState {
    private final List<QueuedPlayer> queuedPlayers = new ArrayList<>();


    public static final Type<RevivalQueueState> TYPE = new Type<>(
            RevivalQueueState::new,
            RevivalQueueState::createFromNbt,
            null
    );

    public QueuedPlayer getPlayer(UUID uuid) {
        return queuedPlayers.stream()
                .filter(p -> p.player().getId().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public QueuedPlayer getPlayer(BlockPos pos, RegistryKey<World> world) {
        return queuedPlayers.stream()
                .filter(p -> p.pos().equals(pos) && p.world().equals(world))
                .findFirst()
                .orElse(null);
    }

    public boolean isQueued(UUID uuid) {
        return queuedPlayers.stream().anyMatch(p -> p.player().getId().equals(uuid));
    }

    public boolean isQueued(BlockPos pos, RegistryKey<World> world) {
        return queuedPlayers.stream().anyMatch(p -> p.pos().equals(pos) && p.world().equals(world));
    }


    public void addQueuedPlayer(QueuedPlayer profile) {
        if (!isQueued(profile.player().getId())) {
            queuedPlayers.add(profile);
            markDirty();
        }
    }

    public void removeQueuedPlayer(UUID uuid, BlockPos pos, RegistryKey<World> world) {
        if (queuedPlayers.removeIf(p -> p.player().getId().equals(uuid)
                && p.pos().equals(pos)
                && p.world().equals(world))) {
            markDirty();
        }
    }

    public void removeQueuedPlayer(BlockPos pos, RegistryKey<World> world) {
        if (queuedPlayers.removeIf(p -> p.pos().equals(pos) && p.world().equals(world))) {
            markDirty();
        }
    }

    public List<QueuedPlayer> getQueuedPlayers() {
        return queuedPlayers;
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

    private NbtCompound queuedPlayerToNbt(QueuedPlayer queuedPlayer) {
        NbtCompound nbt = new NbtCompound();
        nbt.put("Profile", profileToNbt(queuedPlayer.player()));
        nbt.putLong("Pos", queuedPlayer.pos().asLong());
        nbt.putString("World", queuedPlayer.world().getValue().toString());
        return nbt;
    }

    private QueuedPlayer queuedPlayerFromNbt(NbtCompound nbt) {
        GameProfile profile = profileFromNbt(nbt.getCompound("Profile"));
        BlockPos pos = BlockPos.fromLong(nbt.getLong("Pos"));
        Identifier worldId = Identifier.of(nbt.getString("World"));
        RegistryKey<World> world = RegistryKey.of(RegistryKeys.WORLD, worldId);
        return new QueuedPlayer(profile, pos, world);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();
        for (QueuedPlayer queuedPlayer : queuedPlayers) {
            list.add(queuedPlayerToNbt(queuedPlayer));
        }
        nbt.put("QueuedPlayers", list);
        return nbt;
    }

    public static RevivalQueueState createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        RevivalQueueState state = new RevivalQueueState();
        NbtList list = nbt.getList("QueuedPlayers", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : list) {
            state.queuedPlayers.add(state.queuedPlayerFromNbt((NbtCompound) element));
        }
        return state;
    }

    public static RevivalQueueState get(MinecraftServer server) {
        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        return manager.getOrCreate(TYPE, HardcoreRevived.MOD_ID + "_queued_players");
    }
}
