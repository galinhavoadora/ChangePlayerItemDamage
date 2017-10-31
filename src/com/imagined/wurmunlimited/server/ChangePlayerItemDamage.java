package com.imagined.wurmunlimited.server;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;

public class ChangePlayerItemDamage implements Configurable, PreInitable, WurmServerMod {

	private Logger _logger = Logger.getLogger(this.getClass().getName());
	private float itemDamageModifier = 1.0f;
	private float dbItemDamageModifier = 1.0f;
	
	@Override
	public void preInit() {
		this.modifyTempItem();
		this.modifyDbItem();
	}

	@Override
	public void configure(Properties properties) {
		this.itemDamageModifier = Float.valueOf(properties.getProperty("itemDamageModifier", Float.toString(this.itemDamageModifier))).floatValue();
        this.Log("itemDamageModifier: ", this.itemDamageModifier);
        this.dbItemDamageModifier = Float.valueOf(properties.getProperty("dbItemDamageModifier", Float.toString(this.dbItemDamageModifier))).floatValue();
        this.Log("dbItemDamageModifier: ", this.dbItemDamageModifier);
	}
	
	private void Log(String forFeature, float value) {
        this._logger.log(Level.INFO, forFeature + value);
    }

	private void modifyTempItem() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.TempItem");
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.floatType};
            CtMethod method = ex.getMethod("setDamage", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.insertBefore("if (this.ownerId != -10L && "
            		+ "com.wurmonline.server.Server.getInstance().getCreature(this.ownerId).isPlayer() && $1 > 0.0f) {\n"
            		+ "float newval = $1 * " + this.itemDamageModifier + ";\n"
            		+ "logger.log(java.util.logging.Level.INFO, \"Name: \"+this.name+\", Damage: \"+$1+\", new Damage: \"+newval);\n"
            		+ "$1 = newval; }");
            //method.setBody("return true;");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            parameters = new CtClass[]{CtPrimitiveType.floatType, CtPrimitiveType.booleanType};
            method = ex.getMethod("setDamage", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.insertBefore("if (this.ownerId != -10L && "
            		+ "com.wurmonline.server.Server.getInstance().getCreature(this.ownerId).isPlayer() && $1 > 0.0f) {\n"
            		+ "float newval = $1 * " + this.itemDamageModifier + ";\n"
            		+ "logger.log(java.util.logging.Level.INFO, \"Name: \"+this.name+\", Damage: \"+$1+\", new Damage: \"+newval);\n"
            		+ "$1 = newval; }");
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}
	
	private void modifyDbItem() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.DbItem");
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.floatType};
            CtMethod method = ex.getMethod("setDamage", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.insertBefore("if (this.ownerId != -10L && "
            		+ "com.wurmonline.server.Server.getInstance().getCreature(this.ownerId).isPlayer() && $1 > 0.0f) {\n"
            		+ "float newval = $1 * " + this.dbItemDamageModifier + ";\n"
            		+ "logger.log(java.util.logging.Level.INFO, \"Name: \"+this.name+\", Damage: \"+$1+\", new Damage: \"+newval);\n"
            		+ "$1 = newval; }");
            //method.setBody("return true;");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            parameters = new CtClass[]{CtPrimitiveType.floatType, CtPrimitiveType.booleanType};
            method = ex.getMethod("setDamage", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.insertBefore("if (this.ownerId != -10L && "
            		+ "com.wurmonline.server.Server.getInstance().getCreature(this.ownerId).isPlayer() && $1 > 0.0f) {\n"
            		+ "float newval = $1 * " + this.dbItemDamageModifier + ";\n"
            		+ "logger.log(java.util.logging.Level.INFO, \"Name: \"+this.name+\", Damage: \"+$1+\", new Damage: \"+newval);\n"
            		+ "$1 = newval; }");
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}
}
