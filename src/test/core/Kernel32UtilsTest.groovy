package core

import com.sun.jna.Pointer
import spock.lang.Specification

class Kernel32UtilsTest extends Specification {
    def "CreateOrOpenFileMapping does not throw exceptions"() {
        expect:
        Kernel32Utils.createOrOpenFileMapping('test_file_mapping') != null
        !Kernel32Utils.hasError()
    }

    def "CreateOrOpenSemaphore does not throw exceptions"() {
        expect:
        Kernel32Utils.createOrOpenSemaphore('test_semaphore', 1, 1) != null
        !Kernel32Utils.hasError()
    }

    def "Assertion: Kernel primitives couldn't have same names"() {
        def primitiveName = 'primitive_name'
        expect : 'Object with #primitiveName was created and it\'s ok, for a while'
        Pointer semPtr = Kernel32Utils.createOrOpenSemaphore(primitiveName, 1, 1)
        semPtr != null
        !Kernel32Utils.hasError()
        and: 'When creating a primitive with the same name (#primitiveName) occurs, it returns NULL and last Error is #expectedError '
        Pointer fileMappingPtr = Kernel32Utils.createOrOpenFileMapping(primitiveName)
        fileMappingPtr == null
        Kernel32Utils.hasError()


    }
}
