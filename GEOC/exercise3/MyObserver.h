#ifndef _MY_OBSERVER_H
#define _MY_OBSERVER_H

#include <geoc/io/Observer.h>

namespace geoc {
class SceneManager;
class OutputSystem;
class Point;
class Entity;
class CircleEnt;
}

class MyObserver : public geoc::Observer<geoc::Point>,
	           public geoc::Observer<geoc::CircleEnt>,
	           public geoc::Observer<geoc::Entity>
{
    geoc::SceneManager* sceneMgr;
    geoc::OutputSystem* outputSystem;
    geoc::CircleEnt*    circle;
    int num_points;
    
public:
    
    MyObserver(geoc::SceneManager* sceneMgr, geoc::OutputSystem* outputSystem);
    
    void enters(geoc::Point* p);
    void enters(geoc::CircleEnt* c);
    void leaves(geoc::Entity* e);
    void sceneCleared();
};

#endif //_MY_OBSERVER_H
