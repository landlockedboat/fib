#ifndef _EXAMPLE_APP_H
#define _EXAMPLE_APP_H

#include <geoc/geoc.h>
#include <geoc/app/GeocApplication.h>
#include <geoc/io/input/Keyboard.h>

namespace geoc {
class Graphics;
class GeocWidget;
}

class MyObserver;

class ExampleApp : public geoc::GeocApplication
{
    MyObserver* myObserver;
    bool        enable_3d;
    bool        use_cgal;
    
    void parse_args(int argc, char** argv);
    
public:
    
    ExampleApp() : enable_3d(false), use_cgal(false) {}
    
    void init(int argc, char** argv);
    void shutdown();
    void keyPressed(geoc::GeocWidget&, geoc::Keyboard::key key);
};

#endif //_EXAMPLE_APP_H
