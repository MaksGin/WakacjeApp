package com.wakacjeapp.model


// Klasa pozwalająca na odróżnienie dzięki wartości o jakie recycle view chodzi
// W momencie kiedy przypiszemy do niego np cyfre 0 Adapter będzie wiedział w jaki sposób
// recycle view zostanie wyświetlone

class DataItemType {

    companion object{
        const val BEST_SELLER = 0
        const val HOLIDAY = 1
        const val BANNER = 2
    }
}