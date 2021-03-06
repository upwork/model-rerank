/*-
 * #%L
 * Rerank - A library to rerank based on weka models
 * %%
 * Copyright (C) 2017 Upwork Inc.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.upwork.rerank.thrift;

import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;
import org.apache.thrift.scheme.TupleScheme;

public class TInstance implements org.apache.thrift.TBase<TInstance, TInstance._Fields>, java.io.Serializable, Cloneable, Comparable<TInstance> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TInstance");

  private static final org.apache.thrift.protocol.TField INSTANCE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("instanceId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField FEATURES_FIELD_DESC = new org.apache.thrift.protocol.TField("features", org.apache.thrift.protocol.TType.MAP, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TInstanceStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TInstanceTupleSchemeFactory());
  }

  private long instanceId; // required
  private Map<String,TFeatureValue> features; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    INSTANCE_ID((short)1, "instanceId"),
    FEATURES((short)2, "features");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // INSTANCE_ID
          return INSTANCE_ID;
        case 2: // FEATURES
          return FEATURES;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __INSTANCEID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.INSTANCE_ID, new org.apache.thrift.meta_data.FieldMetaData("instanceId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.FEATURES, new org.apache.thrift.meta_data.FieldMetaData("features", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TFeatureValue.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TInstance.class, metaDataMap);
  }

  public TInstance() {
  }

  public TInstance(
    long instanceId,
    Map<String,TFeatureValue> features)
  {
    this();
    this.instanceId = instanceId;
    setInstanceIdIsSet(true);
    this.features = features;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TInstance(TInstance other) {
    __isset_bitfield = other.__isset_bitfield;
    this.instanceId = other.instanceId;
    if (other.isSetFeatures()) {
      Map<String,TFeatureValue> __this__features = new HashMap<String,TFeatureValue>(other.features.size());
      for (Map.Entry<String, TFeatureValue> other_element : other.features.entrySet()) {

        String other_element_key = other_element.getKey();
        TFeatureValue other_element_value = other_element.getValue();

        String __this__features_copy_key = other_element_key;

        TFeatureValue __this__features_copy_value = new TFeatureValue(other_element_value);

        __this__features.put(__this__features_copy_key, __this__features_copy_value);
      }
      this.features = __this__features;
    }
  }

  public TInstance deepCopy() {
    return new TInstance(this);
  }

  @Override
  public void clear() {
    setInstanceIdIsSet(false);
    this.instanceId = 0;
    this.features = null;
  }

  public long getInstanceId() {
    return this.instanceId;
  }

  public void setInstanceId(long instanceId) {
    this.instanceId = instanceId;
    setInstanceIdIsSet(true);
  }

  public void unsetInstanceId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __INSTANCEID_ISSET_ID);
  }

  /** Returns true if field instanceId is set (has been assigned a value) and false otherwise */
  public boolean isSetInstanceId() {
    return EncodingUtils.testBit(__isset_bitfield, __INSTANCEID_ISSET_ID);
  }

  public void setInstanceIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __INSTANCEID_ISSET_ID, value);
  }

  public int getFeaturesSize() {
    return (this.features == null) ? 0 : this.features.size();
  }

  public void putToFeatures(String key, TFeatureValue val) {
    if (this.features == null) {
      this.features = new HashMap<String,TFeatureValue>();
    }
    this.features.put(key, val);
  }

  public Map<String,TFeatureValue> getFeatures() {
    return this.features;
  }

  public void setFeatures(Map<String,TFeatureValue> features) {
    this.features = features;
  }

  public void unsetFeatures() {
    this.features = null;
  }

  /** Returns true if field features is set (has been assigned a value) and false otherwise */
  public boolean isSetFeatures() {
    return this.features != null;
  }

  public void setFeaturesIsSet(boolean value) {
    if (!value) {
      this.features = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case INSTANCE_ID:
      if (value == null) {
        unsetInstanceId();
      } else {
        setInstanceId((Long)value);
      }
      break;

    case FEATURES:
      if (value == null) {
        unsetFeatures();
      } else {
        setFeatures((Map<String,TFeatureValue>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case INSTANCE_ID:
      return Long.valueOf(getInstanceId());

    case FEATURES:
      return getFeatures();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case INSTANCE_ID:
      return isSetInstanceId();
    case FEATURES:
      return isSetFeatures();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TInstance)
      return this.equals((TInstance)that);
    return false;
  }

  public boolean equals(TInstance that) {
    if (that == null)
      return false;

    boolean this_present_instanceId = true;
    boolean that_present_instanceId = true;
    if (this_present_instanceId || that_present_instanceId) {
      if (!(this_present_instanceId && that_present_instanceId))
        return false;
      if (this.instanceId != that.instanceId)
        return false;
    }

    boolean this_present_features = true && this.isSetFeatures();
    boolean that_present_features = true && that.isSetFeatures();
    if (this_present_features || that_present_features) {
      if (!(this_present_features && that_present_features))
        return false;
      if (!this.features.equals(that.features))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_instanceId = true;
    builder.append(present_instanceId);
    if (present_instanceId)
      builder.append(instanceId);

    boolean present_features = true && (isSetFeatures());
    builder.append(present_features);
    if (present_features)
      builder.append(features);

    return builder.toHashCode();
  }

  @Override
  public int compareTo(TInstance other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetInstanceId()).compareTo(other.isSetInstanceId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetInstanceId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.instanceId, other.instanceId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetFeatures()).compareTo(other.isSetFeatures());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFeatures()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.features, other.features);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TInstance(");
    boolean first = true;

    sb.append("instanceId:");
    sb.append(this.instanceId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("features:");
    if (this.features == null) {
      sb.append("null");
    } else {
      sb.append(this.features);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TInstanceStandardSchemeFactory implements SchemeFactory {
    public TInstanceStandardScheme getScheme() {
      return new TInstanceStandardScheme();
    }
  }

  private static class TInstanceStandardScheme extends StandardScheme<TInstance> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TInstance struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // INSTANCE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.instanceId = iprot.readI64();
              struct.setInstanceIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // FEATURES
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map0 = iprot.readMapBegin();
                struct.features = new HashMap<String,TFeatureValue>(2*_map0.size);
                for (int _i1 = 0; _i1 < _map0.size; ++_i1)
                {
                  String _key2;
                  TFeatureValue _val3;
                  _key2 = iprot.readString();
                  _val3 = new TFeatureValue();
                  _val3.read(iprot);
                  struct.features.put(_key2, _val3);
                }
                iprot.readMapEnd();
              }
              struct.setFeaturesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TInstance struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(INSTANCE_ID_FIELD_DESC);
      oprot.writeI64(struct.instanceId);
      oprot.writeFieldEnd();
      if (struct.features != null) {
        oprot.writeFieldBegin(FEATURES_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRUCT, struct.features.size()));
          for (Map.Entry<String, TFeatureValue> _iter4 : struct.features.entrySet())
          {
            oprot.writeString(_iter4.getKey());
            _iter4.getValue().write(oprot);
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TInstanceTupleSchemeFactory implements SchemeFactory {
    public TInstanceTupleScheme getScheme() {
      return new TInstanceTupleScheme();
    }
  }

  private static class TInstanceTupleScheme extends TupleScheme<TInstance> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TInstance struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetInstanceId()) {
        optionals.set(0);
      }
      if (struct.isSetFeatures()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetInstanceId()) {
        oprot.writeI64(struct.instanceId);
      }
      if (struct.isSetFeatures()) {
        {
          oprot.writeI32(struct.features.size());
          for (Map.Entry<String, TFeatureValue> _iter5 : struct.features.entrySet())
          {
            oprot.writeString(_iter5.getKey());
            _iter5.getValue().write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TInstance struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.instanceId = iprot.readI64();
        struct.setInstanceIdIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TMap _map6 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.features = new HashMap<String,TFeatureValue>(2*_map6.size);
          for (int _i7 = 0; _i7 < _map6.size; ++_i7)
          {
            String _key8;
            TFeatureValue _val9;
            _key8 = iprot.readString();
            _val9 = new TFeatureValue();
            _val9.read(iprot);
            struct.features.put(_key8, _val9);
          }
        }
        struct.setFeaturesIsSet(true);
      }
    }
  }

}

