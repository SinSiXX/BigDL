/*
 * Copyright 2016 The BigDL Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intel.analytics.bigdl.nn.keras

import com.intel.analytics.bigdl.nn._
import com.intel.analytics.bigdl.nn.abstractnn.{AbstractModule, TensorModule}
import com.intel.analytics.bigdl.tensor.Tensor
import com.intel.analytics.bigdl.tensor.TensorNumericMath.TensorNumeric

import scala.reflect.ClassTag

object KerasUtils {

  private[keras] def getPadsFromBorderMode(borderMode: String = "valid"): (Int, Int) = {
    if (borderMode == "same") {
      // padH, padW
      (-1, -1)
    } else {
      (0, 0)
    }
  }

  private[keras] def getInitMethod(init: String): InitializationMethod = {
    init.toLowerCase() match {
      case "glorot_uniform" => Xavier
      case "one" => Ones
      case "zero" => Zeros
      case "uniform" => RandomUniform(-0.05, 0.05)
      case "normal" => RandomNormal(0.0, 0.05)
      case _ => throw new IllegalArgumentException(s"Unsupported initialization method: " +
        s"${init.toLowerCase()}")
    }
  }

  private[keras] def getActivation[T : ClassTag] (activation: String)
    (implicit ev: TensorNumeric[T]): AbstractModule[Tensor[T], Tensor[T], T] = {
    if (activation == null) null
    else {
      activation.toLowerCase() match {
          case "tanh" => Tanh[T]()
          case "sigmoid" => Sigmoid[T]()
          case "relu" => ReLU[T]()
          case "softmax" => SoftMax[T]().asInstanceOf[AbstractModule[Tensor[T], Tensor[T], T]]
          case "softplus" => SoftPlus[T]()
          case "softsign" => SoftSign[T]()
          case "hard_sigmoid" => HardSigmoid[T]()
          case _ => throw new IllegalArgumentException(s"Invalid activation: " +
            s"${activation.toLowerCase}. Only simple activations can be constructed using string")
      }
    }
  }

}
