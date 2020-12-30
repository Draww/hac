package com.heretere.hac.core.proxy.versions.sixteen;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.core.proxy.CoreVersionProxy;
import com.heretere.hac.core.proxy.packets.channel.ChannelInjector;
import com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside.AbilitiesPacketFactory;
import com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside.EntityActionPacketFactory;
import com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside.FlyingPacketFactory;
import com.heretere.hac.core.proxy.versions.sixteen.packets.builder.serverside.EntityVelocityPacketFactory;
import com.heretere.hac.core.proxy.versions.sixteen.packets.channel.ChannelInjectorProxy;
import com.heretere.hac.util.plugin.HACPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The type Proxy.
 */
public final class Proxy extends CoreVersionProxy {
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACAPI api;
    /**
     * The parent plugin reference.
     */
    private final @NotNull HACPlugin parent;
    /**
     * The injector used to attach channel handlers to all players.
     */
    private final @NotNull ChannelInjectorProxy channelInjectorProxy;

    /**
     * Instantiates a new Proxy.
     *
     * @param parent the parent
     */
    public Proxy(final @NotNull HACPlugin parent) {
        this.api = Objects.requireNonNull(Bukkit.getServer().getServicesManager().load(HACAPI.class));
        this.parent = parent;
        this.channelInjectorProxy = new ChannelInjectorProxy(this.api, this.parent);
    }

    @Override
    protected void registerPackets() {
        PacketReferences packetReferences = this.api.getPacketReferences();

        //clientside
        packetReferences.getClientSide().getAbilities().register(new AbilitiesPacketFactory());
        packetReferences.getClientSide().getEntityAction().register(new EntityActionPacketFactory());
        packetReferences.getClientSide().getFlying().register(new FlyingPacketFactory());

        //serverside
        packetReferences.getServerSide().getEntityVelocity().register(new EntityVelocityPacketFactory(this.parent));
    }

    @Override
    public @NotNull ChannelInjector getChannelInjector() {
        return this.channelInjectorProxy;
    }

    @Override public void load() {
        //Nothing yet
    }

    @Override public void unload() {
        this.channelInjectorProxy.shutdown();
    }
}
