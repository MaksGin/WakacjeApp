package com.wakacjeapp.model


// Klasa pozwalająca na odróżnienie  o jakie recycle view chodzi
// W momencie kiedy przypiszemy do niego np cyfre 0 Adapter będzie wiedział w jaki sposób
// recycle view zostanie wyświetlone

class DataItemType {

    companion object{
        const val HOLIDAY = 1
        const val BANNER = 2
        const val YOUR_HOLIDAY = 3
        const val TEXT = 4
        const val SEARCH = 5
        const val MENU = 6
    }
}