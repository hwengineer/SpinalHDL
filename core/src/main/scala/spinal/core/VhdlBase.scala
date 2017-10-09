///*
// * SpinalHDL
// * Copyright (c) Dolu, All rights reserved.
// *
// * This library is free software; you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public
// * License as published by the Free Software Foundation; either
// * version 3.0 of the License, or (at your option) any later version.
// *
// * This library is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// * Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public
// * License along with this library.
// */
//
package spinal.core
//
//import scala.collection.mutable
//import scala.collection.mutable.{StringBuilder, ArrayBuffer}
//
///**
// * Created by PIC18F on 07.01.2015.
// */
//
//
trait VhdlBase extends VhdlVerilogBase{
//
  var enumPackageName = "pkg_enum"
  var packageName = "pkg_scala2hdl"


  def emitLibrary(ret: StringBuilder): Unit = {
    ret ++= "library ieee;\n"
    ret ++= "use ieee.std_logic_1164.all;\n"
    ret ++= "use ieee.numeric_std.all;\n"
    ret ++= "\n"
    ret ++= s"library work;\n"
    ret ++= s"use work.$packageName.all;\n"
    ret ++= s"use work.all;\n"
    ret ++= s"use work.$enumPackageName.all;\n\n"
  }
//
//
//  def emitSignal(ref: Node, typeNode: Node): String = {
//    s"  signal ${emitReference(ref)} : ${emitDataType(typeNode)};\n"
//  }
//
//
  def emitClockEdge(clock: String, edgeKind: EdgeKind): String = {
    s"${
      edgeKind match {
        case RISING => "rising_edge"
        case FALLING => "falling_edge"
      }
    }($clock) then\n"
  }

  def emitType(e : Expression) : String = e.getTypeObject match {
    case `TypeBool` => "std_logic"
    case `TypeBits` => s"std_logic_vector${emitRange(e.asInstanceOf[WidthProvider])}"
    case `TypeUInt` => s"unsigned${emitRange(e.asInstanceOf[WidthProvider])}"
    case `TypeSInt` => s"signed${emitRange(e.asInstanceOf[WidthProvider])}"
  }


  def emitEnumLiteral[T <: SpinalEnum](enum : SpinalEnumElement[T],encoding: SpinalEnumEncoding) : String = {
    if(encoding.isNative)
      return enumPackageName + "." + enum.getName()
    else
      return enum.spinalEnum.getName() + "_" + encoding.getName() + "_" + enum.getName()
  }

  def emitEnumType[T <: SpinalEnum](enum : SpinalEnumCraft[T]) : String = emitEnumType(enum.spinalEnum,enum.getEncoding)

  def emitEnumType(enum : SpinalEnum,encoding: SpinalEnumEncoding) : String = {
    if(encoding.isNative)
      return enum.getName()
    else
      return enum.getName() + "_" + encoding.getName() + "_type"
  }

  def emitDataType(node: Expression, constrained: Boolean = true) = node match {
    case bool: Bool => "std_logic"
    case uint: UInt => s"unsigned${if (constrained) emitRange(uint) else ""}"
    case sint: SInt => s"signed${if (constrained) emitRange(sint) else ""}"
    case bits: Bits => s"std_logic_vector${if (constrained) emitRange(bits) else ""}"
    case enum: SpinalEnumCraft[_] => emitEnumType(enum)
  }

  def emitDirection(baseType: BaseType) = baseType.dir match {
    case `in` => "in"
    case `out` => "out"
    case _ => throw new Exception("Unknown direction"); ""
  }
  //    node match {
  //  def emitReference(node: Node): String = {
  //  var referenceSet : mutable.Set[Node with Nameable with ContextUser] = null
  //
  //
  //
  def emitRange(node: WidthProvider) = s"(${node.getWidth - 1} downto 0)"
  //      case n: Nameable with ContextUser => {
//        if(referenceSet != null) referenceSet.add(n)
//        n.getNameElseThrow
//      }
//    }
//  }
//  def emitAssignedReference(node: Node): String = {
//    node match {
//      case n: Nameable with ContextUser => {
//        n.getNameElseThrow
//      }
//    }
//  }
}
