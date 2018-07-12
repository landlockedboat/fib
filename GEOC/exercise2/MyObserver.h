#ifndef _MY_OBSERVER_H
#define _MY_OBSERVER_H

#include <geoc/io/Observer.h>
#include <geoc/scene/Point.h>
#include <geoc/scene/TriangleEnt.h>
#include <geoc/scene/Entity.h>
#include <list>

namespace geoc {
class SceneManager;
class OutputSystem;
}

class MyObserver : public geoc::Observer<geoc::Point>,
	           public geoc::Observer<geoc::TriangleEnt>,
	           public geoc::Observer<geoc::Entity>
{
    geoc::SceneManager* sceneMgr;
    geoc::OutputSystem* outputSystem;
    geoc::TriangleEnt*  triangle;
    int num_points;
    
public:
    
    MyObserver(geoc::SceneManager* sceneMgr, geoc::OutputSystem* outputSystem);
    
    void enters(geoc::Point* p);
    void enters(geoc::TriangleEnt* t);
    void leaves(geoc::Entity* e);
    void sceneCleared();
};

#endif //_MY_OBSERVER_H
