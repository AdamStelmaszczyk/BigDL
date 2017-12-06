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

package com.intel.analytics.bigdl.nn

import com.intel.analytics.bigdl.nn.abstractnn.TensorModule
import com.intel.analytics.bigdl.optim.Regularizer
import com.intel.analytics.bigdl.tensor.{DenseTensorApply, Tensor, TensorFunc6}
import com.intel.analytics.bigdl.tensor.TensorNumericMath.TensorNumeric
import com.intel.analytics.bigdl.utils.Table

import scala.reflect.ClassTag

/**
 * [[Maxout]] A linear maxout layer
 * Maxout layer select the element-wise maximum value of
 * maxoutNumber Linear(inputSize, outputSize) layers
 *
 * @param inputSize: the size the each input sample
 * @param outputSize: the size of the module output of each sample
 * @param maxoutNumber: number of Linear layers to use
 * @param withBias: whether use bias in Linear
 * @param wRegularizer: instance of [[Regularizer]]
 *                    (eg. L1 or L2 regularization), applied to the input weights matrices.
 * @param bRegularizer: instance of [[Regularizer]]
 *                    applied to the bias.
 * @param initWeight: initial weight
 * @param initBias: initial bias
 */
class Maxout[T: ClassTag](inputSize: Int, outputSize: Int, maxoutNumber: Int,
  withBias: Boolean = true, wRegularizer: Regularizer[T] = null,
  bRegularizer: Regularizer[T] = null, initWeight: Tensor[T] = null, initBias: Tensor[T] = null)
  (implicit ev: TensorNumeric[T]) extends TensorModule[T] {
  val layer = Sequential().add(Linear(inputSize, outputSize * maxoutNumber, withBias = withBias,
    wRegularizer = wRegularizer, bRegularizer = bRegularizer, initWeight = initWeight,
    initBias = initBias))
    .add(View(maxoutNumber, outputSize).setNumInputDims(1))
    .add(Max(1, 2))

  override def updateOutput(input: Tensor[T]): Tensor[T] = {
    output = layer.updateOutput(input)
    output
  }

  override def updateGradInput(input: Tensor[T], gradOutput: Tensor[T]): Tensor[T] = {
    gradInput = layer.updateGradInput(input, gradOutput)
    gradInput
  }

  override def accGradParameters(input: Tensor[T], gradOutput: Tensor[T]): Unit = {
    layer.accGradParameters(input, gradOutput)
  }

  override def zeroGradParameters(): Unit = {
    layer.zeroGradParameters()
  }

  override def parameters(): (Array[Tensor[T]], Array[Tensor[T]]) = {
    layer.parameters()
  }

  override def getParametersTable(): Table = {
    layer.getParametersTable()
  }
}

object Maxout {
  def apply[T : ClassTag](inputSize: Int, outputSize: Int, maxoutNumber: Int,
    withBias: Boolean = true, wRegularizer: Regularizer[T] = null,
    bRegularizer: Regularizer[T] = null, initWeight: Tensor[T] = null, initBias: Tensor[T] = null)
    (implicit ev: TensorNumeric[T]): Maxout[T]
    = new Maxout[T](inputSize, outputSize, maxoutNumber, withBias, wRegularizer,
    bRegularizer, initWeight, initBias)
}
