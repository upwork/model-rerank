#!/bin/bash

thrift -r -v -out ../src/main/java --gen java:beans,hashcode models.idl

