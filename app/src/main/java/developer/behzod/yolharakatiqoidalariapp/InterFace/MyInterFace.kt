package developer.behzod.yolharakatiqoidalariapp.InterFace

import developer.behzod.yolharakatiqoidalariapp.Models.Qoyda

interface Rejalashtirihs {
    fun addQoyda(qoyda: Qoyda)
    fun showQoydalar():List<Qoyda>
    fun editQoydalar(qoyda: Qoyda):Int
    fun deleteQoydalar(qoyda: Qoyda)
    fun getQoydalarById(id:Int): Qoyda
}