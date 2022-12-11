package pt.isec.am_tp

import androidx.lifecycle.ViewModel

class SinglePlayerViewModel : ViewModel() {
    var timerLimit = 90
    var operations: MutableList<String> = ArrayList()
    var maxNumber = 10
    var expressions = 2
    var points = 0
    var level = 1
    var bonus = 0
}