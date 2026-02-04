# Getting Started Guide

## Welcome to System Design Mastery! 🚀

This guide will help you navigate the program and start your journey to becoming a system design expert.

---

## Quick Start (First 30 Minutes)

1. **Read the Main README** (15 min)
   - Open: [README.md](README.md)
   - Understand the program structure
   - Note the learning methodology

2. **Review the Cheat Sheet** (10 min)
   - Open: [CHEAT-SHEET.md](CHEAT-SHEET.md)
   - Bookmark it for quick reference
   - Don't memorize yet, just familiarize

3. **Setup Your Tracker** (5 min)
   - Open: [PROGRESS-TRACKER.md](PROGRESS-TRACKER.md)
   - Fill in your start date and goals
   - Print or keep it open in a tab

---

## Your First Week

### Day 1: Foundations

**Morning (1 hour)**:
- Read [01-fundamentals/README.md](01-fundamentals/README.md)
- Focus on scalability concepts
- Take notes on key concepts

**Afternoon (1 hour)**:
- Practice back-of-envelope calculations
- Do Exercise 1: Estimate Netflix bandwidth
- Review your calculations

**Evening (30 min)**:
- Watch: "Scalability for Dummies" talks
- Review latency numbers from cheat sheet

---

### Day 2: Building Blocks

**Morning (1 hour)**:
- Read [02-core-building-blocks/README.md](02-core-building-blocks/README.md)
- Study load balancers and caching
- Draw diagrams of each concept

**Afternoon (1 hour)**:
- Practice: Design caching strategy for a blog
- Compare your design with notes
- Identify gaps in understanding

**Evening (30 min)**:
- Review what you learned
- Quiz yourself on caching strategies

---

### Day 3: First System Design

**Morning (30 min)**:
- Read [12-interview-practice/FRAMEWORK.md](12-interview-practice/FRAMEWORK.md)
- Understand the 45-minute structure
- Note the time allocation

**Afternoon (1.5 hours)**:
- Design your first system: URL Shortener
- Use: [10-classic-designs/URL-SHORTENER.md](10-classic-designs/URL-SHORTENER.md)
- Follow the framework strictly
- Time yourself: 45 minutes
- Then review the reference solution

**Evening (30 min)**:
- Reflect on what was difficult
- Note areas to improve
- Update your progress tracker

---

### Day 4-7: Continue Foundations

Repeat the pattern:
- **Morning**: Study new concept
- **Afternoon**: Practice problem
- **Evening**: Review and reflect

Follow: [PROGRESS-TRACKER.md](PROGRESS-TRACKER.md) for detailed daily plans

---

## Study Techniques

### 1. The Feynman Technique

After learning a concept:

```
Step 1: Learn the concept
   ↓
Step 2: Explain it in simple terms (as if to a 5-year-old)
   ↓
Step 3: Identify gaps in your explanation
   ↓
Step 4: Go back and re-study those gaps
   ↓
Step 5: Simplify and use analogies
```

**Example**:
- **Concept**: Load balancing
- **Simple explanation**: "Like a traffic cop directing cars to different lanes so no single lane gets too crowded"
- **Gap identified**: "But how does it know which server to pick?"
- **Re-study**: Learn about algorithms (round robin, least connections, etc.)

---

### 2. Active Recall

Don't just read. Test yourself:

**After each section**:
- Close your notes
- Draw the diagram from memory
- Explain the concept out loud
- Write down what you remember
- Check against notes

**Example Daily Quiz**:
```
1. Draw a load balancer architecture from memory
2. Explain 3 caching strategies without looking
3. Calculate storage for 1M users × 100KB per user
4. When would you choose NoSQL over SQL?
5. What are the trade-offs of consistent hashing?
```

---

### 3. Spaced Repetition

Review concepts at increasing intervals:

```
Day 1:  Learn concept A
Day 2:  Review concept A (5 min)
Day 4:  Review concept A (3 min)
Day 8:  Review concept A (2 min)
Day 30: Review concept A (1 min)
```

**Use**: Create flashcards for key concepts
- Front: "What is CAP theorem?"
- Back: "In distributed systems, choose 2 of 3: Consistency, Availability, Partition Tolerance"

---

### 4. Deliberate Practice

**Not Enough**:
- Just reading materials
- Watching videos passively
- "Understanding" concepts

**Required**:
- **Design 1 system per day** (even simple ones)
- **Time yourself** (45 minutes)
- **Review critically** (what could be better?)
- **Compare** with reference solutions
- **Redo** problems you struggled with

---

## Daily Routine (2-3 hours)

### Option A: Morning Person
```
6:00 AM - 6:30 AM:  Review previous day (spaced repetition)
6:30 AM - 7:30 AM:  Study new concept + take notes
7:30 AM - 8:15 AM:  Practice problem (45 min timed)
8:15 AM - 8:30 AM:  Review solution, note improvements
```

### Option B: Evening Person
```
7:00 PM - 7:30 PM:  Review previous concepts
7:30 PM - 8:30 PM:  Study new concept + draw diagrams
8:30 PM - 9:15 PM:  Practice problem (45 min timed)
9:15 PM - 9:30 PM:  Reflect and update tracker
```

### Option C: Split Sessions
```
Morning (1 hour):   Study theory + take notes
Lunch (30 min):     Review previous day's concepts
Evening (1 hour):   Practice design problem + review
```

**Pick what works for your schedule and stick to it!**

---

## Tools You'll Need

### Must-Have
- **Whiteboard or paper**: For drawing diagrams
- **Timer**: For 45-minute practice sessions
- **Notebook**: For taking notes and calculations

### Optional but Helpful
- **Drawing tool**: Draw.io, Excalidraw, or Lucidchart
- **Flashcard app**: Anki for spaced repetition
- **Study buddy**: For mock interviews
- **Screen recording**: Record yourself explaining designs

---

## Common Mistakes to Avoid

### ❌ Mistake 1: Passive Reading
**Problem**: Just reading without practicing  
**Fix**: Design 1 system per day, no exceptions

### ❌ Mistake 2: No Time Limits
**Problem**: Spending 3 hours on one design  
**Fix**: Always use 45-minute timer

### ❌ Mistake 3: Skipping Fundamentals
**Problem**: Jumping to complex designs without basics  
**Fix**: Complete Phase 1 fully before moving on

### ❌ Mistake 4: Not Drawing Diagrams
**Problem**: Only writing text descriptions  
**Fix**: Draw every architecture, data flow, component

### ❌ Mistake 5: Isolation
**Problem**: Studying alone without feedback  
**Fix**: Find study partner, do mock interviews

### ❌ Mistake 6: Perfectionism
**Problem**: Trying to memorize everything  
**Fix**: Focus on understanding > memorization

### ❌ Mistake 7: No Reflection
**Problem**: Rushing to next problem without learning  
**Fix**: Spend 10 min reviewing what you struggled with

---

## Checkpoint Assessments

### Week 2 Self-Test

Can you do these in 10 minutes?

- [ ] Draw a 3-tier web architecture from memory
- [ ] Calculate storage for 1M users × 5 posts × 1KB each
- [ ] Explain CAP theorem with 2 examples
- [ ] List 3 caching strategies with pros/cons
- [ ] Choose SQL or NoSQL for 5 different scenarios

**If you can't**: Review Phase 1 again

---

### Week 4 Self-Test

Can you design these in 45 minutes?

- [ ] URL Shortener (complete design)
- [ ] Pastebin
- [ ] Rate Limiter

**Target score**: 25+/35 on self-assessment rubric

**If below 25**: Practice more simple designs

---

### Week 8 Self-Test

Can you design these in 45 minutes?

- [ ] Twitter (complete design)
- [ ] Instagram
- [ ] Uber

**Target score**: 30+/35 on self-assessment rubric

**Ready for interviews**: Score 30+ consistently

---

## When You Feel Stuck

### "I don't understand concept X"

1. Re-read the section slowly
2. Watch a YouTube video on the topic
3. Find real-world examples (engineering blogs)
4. Explain it to someone else (or rubber duck)
5. Draw a diagram
6. Ask in study group or forum

### "I can't finish designs in 45 minutes"

1. Start with simpler problems
2. Use the framework strictly
3. Don't dive into details too early
4. Practice time allocation
5. Redo problems to build speed

### "I keep forgetting concepts"

1. Use spaced repetition (flashcards)
2. Teach concepts to others
3. Create analogies/stories
4. Draw from memory daily
5. Review cheat sheet regularly

### "I'm overwhelmed"

1. Take a break (1-2 days)
2. Review progress so far
3. Reduce daily study time
4. Focus on one concept at a time
5. Remember: This is a marathon, not a sprint

---

## Success Stories Pattern

**Week 1-2**: "I feel like I'm drinking from a firehose"
- Normal! Keep going, it gets easier

**Week 3-4**: "Things are clicking now"
- Concepts start connecting

**Week 5-6**: "I can design simple systems confidently"
- Building momentum

**Week 7-8**: "I'm ready for interviews"
- Confidence in complex designs

---

## Study Groups

### Find/Create a Study Group

**Benefits**:
- Mock interviews
- Different perspectives
- Accountability
- Explaining reinforces learning

**How to run group sessions** (2 hours weekly):
```
15 min: Each person shares what they learned
30 min: Discuss a concept together
45 min: Mock interview (one person)
30 min: Review and feedback
```

**Online communities**:
- Discord servers for system design
- Reddit r/cscareerquestions
- LeetCode forums
- GitHub study groups

---

## Resources Beyond This Program

### Books (In Order of Priority)

1. **Designing Data-Intensive Applications** - Martin Kleppmann
   - The bible of system design
   - Deep, comprehensive
   - Read chapters 1-9

2. **System Design Interview** - Alex Xu
   - Practical interview focus
   - Good diagrams
   - Easy to understand

3. **Database Internals** - Alex Petrov
   - Deep dive into databases
   - For advanced learners

### Blogs to Follow

- **High Scalability**: http://highscalability.com
- **Netflix Tech Blog**: https://netflixtechblog.com
- **Uber Engineering**: https://eng.uber.com
- **Airbnb Engineering**: https://airbnb.io
- **AWS Architecture Blog**: https://aws.amazon.com/blogs/architecture

### Videos

- **Gaurav Sen** - System design basics
- **Tech Dummies** - Narendra L
- **InfoQ Talks** - Real engineering talks

---

## Final Tips

### Before Interview

**1 week before**:
- [ ] Review cheat sheet daily
- [ ] Do 1 mock interview
- [ ] Practice 1 design daily

**1 day before**:
- [ ] Review cheat sheet
- [ ] Practice 1 easy problem (confidence boost)
- [ ] Get good sleep
- [ ] Don't cram

**Day of**:
- [ ] Review latency numbers
- [ ] Light breakfast
- [ ] Stay calm
- [ ] Trust your preparation

### During Interview

**Do**:
- ✅ Think out loud
- ✅ Ask clarifying questions
- ✅ Draw diagrams
- ✅ Discuss trade-offs
- ✅ Listen to interviewer
- ✅ Manage time

**Don't**:
- ❌ Jump into details immediately
- ❌ Stay silent
- ❌ Ignore interviewer hints
- ❌ Over-engineer
- ❌ Panic if you don't know something

---

## Your Learning Contract

**I commit to**:
- [ ] Study ___ hours per day for ___ weeks
- [ ] Complete all practice problems
- [ ] Do at least 3 mock interviews
- [ ] Review concepts using spaced repetition
- [ ] Not give up when it gets hard
- [ ] Ask for help when stuck

**Signed**: ___________  
**Date**: ___________

---

## Let's Begin!

**Your next steps** (right now):

1. ✅ Read this guide (you just did!)
2. ⬜ Open [01-fundamentals/README.md](01-fundamentals/README.md)
3. ⬜ Start reading about scalability
4. ⬜ Set your timer for 1 hour
5. ⬜ Take your first notes

**Remember**: Every expert was once a beginner. The only difference is they started.

---

**Good luck! You've got this! 🚀**

*Questions? Stuck? Re-read the relevant section or find a study buddy to discuss!*
