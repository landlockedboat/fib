#ifndef _EXAMPLE_APP_H
#define _EXAMPLE_APP_H

#include <geoc/geoc.h>
#include <geoc/app/GeocApplication.h>

class MyObserver;

class ExampleApp : public geoc::GeocApplication
{
    MyObserver* myObserver;
    
public:
    void init(int argc, char** argv);
    void shutdown();
};

#endif //_EXAMPLE_APP_H
