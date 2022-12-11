package pt.isec.am_tp

import androidx.lifecycle.ViewModel

class SinglePlayerViewModel : ViewModel() {
    var timer = 60
    var operations: MutableList<String> = ArrayList()
    var maxNumber = 10
    var points = 0
}