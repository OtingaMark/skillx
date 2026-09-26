package com.skillx.server.features.points.domain.service
class PointTransferService { fun validateTransfer(fromBalance: Int, amount: Int): Boolean = fromBalance >= amount }
