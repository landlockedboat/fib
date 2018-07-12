
#include "GeneralFunctions.cc"


int main(int argc, char* argv[]) {
    if (argc != 2) merror("mal indicado");
    string direccion = argv[1];
    vector<string> datos = getFileNamesFromDirPath(direccion);
    cout << "Los datos que tenemos son" << endl;
    for (int i = 0; i < datos.size(); ++i) {
      cout << datos[i] << endl;  
    }
}