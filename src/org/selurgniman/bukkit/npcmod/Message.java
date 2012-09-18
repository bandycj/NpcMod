/**
 * 
 */
package org.selurgniman.bukkit.npcmod;

import org.bukkit.configuration.Configuration;

/**
 * @author <a href="mailto:selurgniman@selurgniman.org">Selurgniman</a> Created
 *         on: Dec 18, 2011
 */
public enum Message {
	PREFIX("messages.NpcMod.prefix"),
	NPCMOD_ENABLED_MESSAGE("messages.enabled"),
    NPCMOD_DISABLED_MESSAGE("messages.disabled"),
    NPCMOD_WORLDS_MESSAGE("messages.NpcMod.worlds"),
	LACK_PERMISSION_MESSAGE("messages.permission"),
	CURRENTZONE_MESSAGE("messages.NpcMod.currentzone"),
	ZONE_MESSAGE("messages.NpcMod.zone"),
	RADIUS_MESSAGE("messages.NpcMod.radius"),
	DEBUG_MESSAGE("messages.debug");

	private final String key;
	private String mMessage = "";

	private Message(String key) {
		this.key = key;
	}

	private String getKey() {
		return key;
	}
	
	private void setMessage(String message){
		mMessage = message;
	}

	public static void populateMessages(Configuration config) {
		for (Message message:Message.values()){
			message.setMessage(config.getString(message.getKey()));
		}
	}
	
	public String with(Object... values) {
		switch (values.length) {
			case 0: {
				return mMessage;
			}
			case 1: {
				return String.format(mMessage, values[0]);
			}
			case 2: {
				return String.format(mMessage, values[0], values[1]);
			}
			case 3: {
				return String.format(mMessage, values[0], values[1], values[2]);
			}
			default: {
				return "Unknown message format";
			}
		}
	}

	@Override
	public String toString() {
		return mMessage;
	}
}
