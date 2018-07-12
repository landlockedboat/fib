#ifndef _MY_OBSERVER_H
#define _MY_OBSERVER_H

#include <geoc/io/Observer.h>
#include <geoc/scene/TriangulationEnt.h>
#include <list>

namespace geoc {
class SceneManager;
class OutputSystem;
class Point;
class Graphics;
}

class MyObserver : public geoc::Observer<geoc::TriangulationEnt>
{
    geoc::SceneManager* sceneMgr;
    geoc::OutputSystem* outputSystem;
    geoc::Graphics*     gfx;
    bool enable_3d;
    
    geoc::TriangulationEnt* t;
    
public:
    
    MyObserver(geoc::SceneManager* sceneMgr,
	       geoc::OutputSystem* outputSystem,
	       geoc::Graphics* gfx,
	       bool enable_3d);
    
    void enters(geoc::TriangulationEnt* t);
    
    void sceneCleared();
    
    void setDrawMode(geoc::TRIANGULATION_DRAW_MODE mode);
};

#endif //_MY_OBSERVER_H
