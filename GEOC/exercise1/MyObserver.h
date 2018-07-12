#ifndef _MY_OBSERVER_H
#define _MY_OBSERVER_H

#include <geoc/io/Observer.h>
#include <geoc/scene/LineSegmentEnt.h>
#include <geoc/scene/Entity.h>

namespace geoc {
class SceneManager;
class OutputSystem;
}

class MyObserver : public geoc::Observer<geoc::LineSegmentEnt>,
	public geoc::Observer<geoc::Entity>
{
    geoc::SceneManager*   sceneMgr;
    geoc::OutputSystem*   outputSystem;
    geoc::LineSegmentEnt* previous_segment;
    int num_segments;
    int pair_num;
    
public:
    
    MyObserver(geoc::SceneManager* sceneMgr, geoc::OutputSystem* outputSystem);
    ~MyObserver();
    
    void enters(geoc::LineSegmentEnt* s);
    void leaves(geoc::Entity* e);
    void sceneCleared();
};

#endif //_MY_OBSERVER_H
