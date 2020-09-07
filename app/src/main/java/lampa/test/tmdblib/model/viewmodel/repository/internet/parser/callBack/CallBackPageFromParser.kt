package lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack

interface CallBackPageFromParser {

    fun onPageFind(link: String)
    fun onPageNotFound()
}